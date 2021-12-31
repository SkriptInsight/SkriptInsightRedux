package io.github.skriptinsight.redux.file.workspace

interface WorkspaceLogger {
    fun info(message: String)
    fun warning(message: String)
    fun error(message: String)
}

