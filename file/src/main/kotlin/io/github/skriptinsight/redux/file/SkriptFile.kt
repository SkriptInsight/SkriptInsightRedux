package io.github.skriptinsight.redux.file

import io.github.skriptinsight.redux.core.utils.ExtraDataContainer
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.SkriptNodeUtils
import io.github.skriptinsight.redux.file.node.indentation.IndentationUtils.computeNodeDataParents
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import java.io.File
import java.net.URI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Represents a **Skript file**.
 * @param url The path to the file
 * @param nodes The data for each node (line)
 * @author NickAcPT
 */
class SkriptFile private constructor(val url: URI, val workspace: SkriptWorkspace, val nodes: ConcurrentMap<Int, AbstractSkriptNode>):
    ExtraDataContainer {
    init {
        workspace.addFile(this)
        computeNodeDataParents(this)
    }

    companion object {
        private fun fromText(url: URI, workspace: SkriptWorkspace, lines: List<String>): SkriptFile {
            return SkriptFile(
                url,
                workspace,
                ConcurrentHashMap<Int, AbstractSkriptNode>().apply {
                    lines.forEachIndexed { i, it -> this[i] = SkriptNodeUtils.createSkriptNodeFromLine(workspace, i, it) }
                }
            )
        }

        fun fromFile(url: URI, workspace: SkriptWorkspace): SkriptFile {
            return fromText(url, workspace, File(url).readLines())
        }

        fun fromFile(file: File, workspace: SkriptWorkspace): SkriptFile {
            return fromText(file.toURI(), workspace, file.readLines())
        }

        fun fromText(url: URI, workspace: SkriptWorkspace, text: String): SkriptFile {
            return fromText(url, workspace, text.lines())
        }

        fun fromText(workspace: SkriptWorkspace, text: String): SkriptFile {
            return fromText(
                URI("file://${UUID.randomUUID()}"),
                workspace,
                text.lines()
            )
        }

    }

    /**
     * Computes the root nodes (aka the nodes without parents)
     */
    val rootNodes
        get() = nodes.values.filter { it.parent == null }

    operator fun get(index: Int): AbstractSkriptNode? {
        return nodes[index]
    }

    fun <R> runProcess(process: SkriptFileProcess<R>): List<R> {
        return workspace.runProcess(this, process)
    }

    override val extraData: MutableMap<String, Any> = ConcurrentHashMap()

}

