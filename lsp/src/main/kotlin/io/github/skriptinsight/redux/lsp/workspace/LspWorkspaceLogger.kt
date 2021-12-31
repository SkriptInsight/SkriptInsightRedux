package io.github.skriptinsight.redux.lsp.workspace

import io.github.skriptinsight.redux.file.workspace.WorkspaceLogger
import org.eclipse.lsp4j.MessageParams
import org.eclipse.lsp4j.MessageType

class LspWorkspaceLogger(private val workspace: LspSkriptWorkspace) : WorkspaceLogger {
    override fun info(message: String) {
        internalLog(MessageType.Info, message)
    }

    override fun warning(message: String) {
        internalLog(MessageType.Warning, message)
    }

    override fun error(message: String) {
        internalLog(MessageType.Error, message)
    }

    private fun internalLog(type: MessageType, message: String) {
        workspace.client.logMessage(
            MessageParams(
                type,
                message
            )
        )
    }
}