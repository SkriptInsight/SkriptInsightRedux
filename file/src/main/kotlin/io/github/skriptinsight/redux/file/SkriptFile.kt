package io.github.skriptinsight.redux.file

import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.SkriptNodeUtils
import io.github.skriptinsight.redux.file.node.indentation.IndentationUtils.computeNodeDataParents
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import io.github.skriptinsight.redux.file.work.impl.FileProcessCallable
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import java.io.File
import java.net.URI
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ForkJoinPool
import kotlin.reflect.KProperty

/**
 * Represents a **Skript file**.
 * @param url The path to the file
 * @param nodes The data for each node (line)
 * @author NickAcPT
 */
class SkriptFile(val url: URI, val workspace: SkriptWorkspace, val nodes: ConcurrentMap<Int, AbstractSkriptNode>) {
    init {
        workspace.addFile(this)
        computeNodeDataParents(this)
    }

    private val extraData = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(property: KProperty<T>): T? {
        return get(property.name)
    }

    operator fun <T> set(property: KProperty<T>, value: T?) {
        set(property.name, value)
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(name: String): T? {
        return extraData[name] as? T?
    }

    operator fun set(name: String, value: Any?) {
        if (value == null) {
            extraData.remove(name)
        } else {
            extraData[name] = value
        }
    }

    companion object {
        private fun fromText(url: URI, workspace: SkriptWorkspace, lines: List<String>): SkriptFile {
            return SkriptFile(
                url,
                workspace,
                ConcurrentHashMap<Int, AbstractSkriptNode>().apply {
                    lines.forEachIndexed { i, it -> this[i] = SkriptNodeUtils.createSkriptNodeFromLine(i, it) }
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
        val map = nodes.map {
            FileProcessCallable(process, this, it.key, it.value.rawContent, it.value)
        }
        return ForkJoinPool.commonPool().invokeAll(map).map { it.get() }
    }

}

