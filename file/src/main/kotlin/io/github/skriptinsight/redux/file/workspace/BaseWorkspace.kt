package io.github.skriptinsight.redux.file.workspace

import io.github.skriptinsight.redux.file.SkriptFile
import java.net.URI

abstract class BaseWorkspace {
    abstract val language: WorkspaceLanguage
    private val files: MutableMap<URI, SkriptFile> = mutableMapOf()

    fun addFile(file: SkriptFile) {
        files[file.url] = file
    }

    fun getFile(url: URI): SkriptFile? {
        return files[url]
    }
}