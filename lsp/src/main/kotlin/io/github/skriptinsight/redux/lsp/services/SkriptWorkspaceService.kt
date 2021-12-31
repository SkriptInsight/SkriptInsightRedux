package io.github.skriptinsight.redux.lsp.services

import io.github.skriptinsight.redux.lsp.workspace.LspSkriptWorkspace
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.DidChangeWatchedFilesParams
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageClientAware
import org.eclipse.lsp4j.services.WorkspaceService

class SkriptWorkspaceService(val workspace: LspSkriptWorkspace) : WorkspaceService, LanguageClientAware {

    lateinit var client: LanguageClient

    override fun connect(client: LanguageClient) {
        this.client = client
    }

    override fun didChangeConfiguration(params: DidChangeConfigurationParams) {
        workspace.logger.info("User changed configuration")
    }

    override fun didChangeWatchedFiles(params: DidChangeWatchedFilesParams) {
        workspace.logger.info("User changed watched files")
    }

    fun onClientInitialized(client: LanguageClient) {

    }

}
