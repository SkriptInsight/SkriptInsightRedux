package io.github.skriptinsight.redux.lsp.workspace

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.node.SkriptNodeUtils
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import io.github.skriptinsight.redux.lsp.extensions.toInsightRange
import org.eclipse.lsp4j.MessageParams
import org.eclipse.lsp4j.MessageType
import org.eclipse.lsp4j.TextDocumentContentChangeEvent
import org.eclipse.lsp4j.services.LanguageClient
import java.net.URI
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

object WorkspaceActionHandler {

    fun onFileOpen(workspace: SkriptWorkspace, uri: String, text: String) {
        val file = SkriptFile.fromText(URI(uri), workspace, text)
        workspace.addFile(file)
    }

    fun onFileClose(workspace: SkriptWorkspace, uri: String) {
        workspace.removeFile(URI(uri))
    }

    @OptIn(ExperimentalTime::class)
    fun onFileChange(workspace: SkriptWorkspace, uri: String, contentChanges: List<TextDocumentContentChangeEvent>) {
        val file = workspace.getFile(URI(uri)) ?: return


        val resultTime = measureTime {
            contentChanges.forEach { change ->
                file.applyFileChange(workspace, change)
            }
        }

        workspace.get<LanguageClient>("client")?.logMessage(
            MessageParams(
                MessageType.Info,
                "Handling of ${contentChanges.size} change(s) took ${resultTime.toDouble(DurationUnit.MILLISECONDS)} ms"
            )
        )

    }

    @OptIn(ExperimentalTime::class)
    private fun SkriptFile.applyFileChange(workspace: SkriptWorkspace, change: TextDocumentContentChangeEvent) {
        val lines = change.text.lines()
        // If there is no change range, this means that the entire file has been changed (e.g. by opening a new file)
        if (change.range == null) {
            lines.forEachIndexed { i, lineContent ->
                nodes[i] = SkriptNodeUtils.createSkriptNodeFromLine(workspace, i, lineContent)
            }
        } else {
            // There has been an incremental change to the file
            val startLine = change.range.start.line
            val endLine = change.range.end.line

            val endingLineDiff = endLine - startLine
            val changedLineCount = endLine - startLine + 1
            val oldNodes = nodes.filterKeys { it in startLine..(startLine + endingLineDiff) }

            // Remove the old changed nodes from the file since they are no longer valid
            oldNodes.keys.forEach { line ->
                nodes[line]?.parent?.children?.remove(nodes[line])
                nodes.remove(line)
            }

            val oldNodesAsString = oldNodes.values.joinToString(System.lineSeparator()) { it.rawContent }

            // Compute edit range relative to the old nodes
            val editRange = change.range.toInsightRange().apply {
                start.lineNumber -= startLine
                end.lineNumber -= startLine
            }

            // Compute the indices of the changed lines
            val startPosition = editRange.start.resolvePosition(oldNodesAsString)
            val endPosition = editRange.end.resolvePosition(oldNodesAsString)

            val builder = StringBuilder(oldNodesAsString)

            // Apply the changes to the raw content of the nodes
            val replaceResult = measureTime {
                kotlin.runCatching {
                    builder.delete(startPosition, endPosition)
                    builder.insert(startPosition, change.text)
                }
            }

            workspace.get<LanguageClient>("client")?.logMessage(
                MessageParams(
                    MessageType.Info,
                    "Replacing of $changedLineCount line(s) took ${replaceResult.toDouble(DurationUnit.MILLISECONDS)} ms"
                )
            )

            val patchedStrings = builder.lines()

            // Check if new lines have been added to the file
            if (patchedStrings.size > changedLineCount) {
                // New lines have been added to the file
                // So we need to shift the old nodes to the new line numbers

                val newLineCount = patchedStrings.size - changedLineCount
                val shiftRangeStart = startLine + 1

                for (i in nodes.size downTo shiftRangeStart) {
                    val currentNode = nodes[i] ?: continue
                    currentNode.lineNumber += newLineCount

                    currentNode.ranges.forEach {
                        it.start.lineNumber += newLineCount
                        it.end.lineNumber += newLineCount
                    }

                    nodes[i + newLineCount] = currentNode
                    currentNode.parent?.children?.remove(currentNode)
                    nodes.remove(i)
                }
            } else if (patchedStrings.size < changedLineCount) {
                // Old lines have been removed from the file
                // So we need to shift the old nodes to the new line numbers

                val newLineCount = changedLineCount - patchedStrings.size
                val shiftRangeStart = startLine + 1

                for (i in shiftRangeStart..nodes.keys.maxOf { it }) {
                    val currentNode = nodes[i] ?: continue
                    currentNode.lineNumber -= newLineCount

                    currentNode.ranges.forEach {
                        it.start.lineNumber -= newLineCount
                        it.end.lineNumber -= newLineCount
                    }

                    nodes[i - newLineCount] = currentNode
                    currentNode.parent?.children?.remove(currentNode)
                    nodes.remove(i)
                }
            }

            // Add the new nodes to the file
            patchedStrings.forEachIndexed { i, lineContent ->
                val finalLineNumber = startLine + i

                val newNode = measureTimedValue {
                    SkriptNodeUtils.createSkriptNodeFromLine(
                        workspace,
                        finalLineNumber, lineContent
                    )
                }

                workspace.get<LanguageClient>("client")?.logMessage(
                    MessageParams(
                        MessageType.Info,
                        "Created new node for line $finalLineNumber in ${newNode.duration.toDouble(DurationUnit.MILLISECONDS)} ms"
                    )
                )
                nodes[finalLineNumber] = newNode.value
            }
        }
        //IndentationUtils.computeNodeDataParents(this)
    }

}