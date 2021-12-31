package io.github.skriptinsight.redux.file.workspace

import io.github.skriptinsight.redux.core.utils.ExtraDataContainer
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import io.github.skriptinsight.redux.file.workspace.providers.SectionParser
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

abstract class BaseWorkspace : ExtraDataContainer {
    abstract val language: WorkspaceLanguage

    abstract val sectionProviders: List<SectionParser>

    private val files: MutableMap<URI, SkriptFile> = mutableMapOf()

    open val logger: WorkspaceLogger = FallbackWorkspaceLogger

    fun addFile(file: SkriptFile) {
        files[file.url] = file
    }

    fun getFile(url: URI): SkriptFile? {
        return files[url]
    }

    fun removeFile(uri: URI) {
        files.remove(uri)
    }

    abstract fun <R> runProcess(skriptFile: SkriptFile, process: SkriptFileProcess<R>): List<R>

    override val extraData: MutableMap<String, Any> = ConcurrentHashMap()
}