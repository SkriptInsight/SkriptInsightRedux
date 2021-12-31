package io.github.skriptinsight.redux.file.workspace

object FallbackWorkspaceLogger : WorkspaceLogger {
    override fun info(message: String) {
        internalLog("INFO", message)
    }

    override fun warning(message: String) {
        internalLog("WARN", message)
    }

    override fun error(message: String) {
        internalLog("ERROR", message)
    }

    private fun internalLog(type: String, message: String) {
        println("[$type] $message")
    }
}