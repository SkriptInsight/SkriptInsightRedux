package io.github.skriptinsight.redux.lsp.services

import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.DidChangeWatchedFilesParams
import org.eclipse.lsp4j.MessageParams
import org.eclipse.lsp4j.MessageType
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageClientAware
import org.eclipse.lsp4j.services.WorkspaceService

object SkriptWorkspaceService : WorkspaceService, LanguageClientAware {

    lateinit var client: LanguageClient

    override fun connect(client: LanguageClient) {
        this.client = client
    }

    override fun didChangeConfiguration(params: DidChangeConfigurationParams) {
        client.logMessage(MessageParams(MessageType.Info, "didChangeConfiguration"))
    }

    override fun didChangeWatchedFiles(params: DidChangeWatchedFilesParams) {
        client.logMessage(MessageParams(MessageType.Info, "didChangeWatchedFiles"))
    }

}
