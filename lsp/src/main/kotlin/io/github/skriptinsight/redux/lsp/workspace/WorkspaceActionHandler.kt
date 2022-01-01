package io.github.skriptinsight.redux.lsp.workspace

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.analysis.managers.InspectionManager
import io.github.skriptinsight.redux.file.node.SkriptNodeUtils
import io.github.skriptinsight.redux.file.node.indentation.IndentationUtils
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import io.github.skriptinsight.redux.lsp.extensions.toInsightRange
import org.eclipse.lsp4j.TextDocumentContentChangeEvent
import java.net.URI
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

object WorkspaceActionHandler {

    fun onFileOpen(workspace: SkriptWorkspace, uri: String, text: String) {
        val file = SkriptFile.fromText(URI(uri), workspace, text)
        workspace.addFile(file)
        InspectionManager.inspectFile(file)
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
            IndentationUtils.computeNodeDataParents(file)
        }

        workspace.logger.info(
            "Handling of ${contentChanges.size} change(s) took ${resultTime.toDouble(DurationUnit.MILLISECONDS)} ms"
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
            InspectionManager.inspectFile(this)
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
            val replaceResultTime = measureTime {
                builder.delete(startPosition, endPosition)
                builder.insert(startPosition, change.text)
            }

            workspace.logger.info("Replacing of $changedLineCount lines took ${replaceResultTime.toDouble(DurationUnit.MILLISECONDS)} ms")

            val patchedStrings = builder.lines()

            // Check if new lines have been added to the file
            if (patchedStrings.size > changedLineCount) {
                // New lines have been added to the file
                // So we need to shift the old nodes to the new line numbers

                handleShiftingForExistingLineNodes(patchedStrings, changedLineCount, startLine, true)
            } else if (patchedStrings.size < changedLineCount) {
                // Old lines have been removed from the file
                // So we need to shift the old nodes to the new line numbers
                handleShiftingForExistingLineNodes(patchedStrings, changedLineCount, startLine, false)
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

                workspace.logger.info("Created new node for line $finalLineNumber in ${replaceResultTime.toDouble(DurationUnit.MILLISECONDS)} ms")

                nodes[finalLineNumber] = newNode.value
            }

            InspectionManager.inspectFile(this, startLine, endLine)
        }
    }

    private fun SkriptFile.handleShiftingForExistingLineNodes(
        patchedStrings: List<String>,
        changedLineCount: Int,
        startLine: Int,
        added: Boolean
    ) {
        val newLineCount = if (added) patchedStrings.size - changedLineCount else changedLineCount - patchedStrings.size
        val shiftRangeStart = startLine + 1

        var progression = maxLineNumber downTo shiftRangeStart
        if (!added) progression = progression.reversed()

        for (i in progression) {
            val currentNode = nodes[i] ?: continue
            val offsetAmount = if (added) newLineCount else -newLineCount

            currentNode.lineNumber += offsetAmount

            currentNode.ranges.forEach {
                it.start.lineNumber += offsetAmount
                it.end.lineNumber += offsetAmount
            }

            nodes[i + offsetAmount] = currentNode
            currentNode.parent?.children?.remove(currentNode)
            nodes.remove(i)
        }
    }

}