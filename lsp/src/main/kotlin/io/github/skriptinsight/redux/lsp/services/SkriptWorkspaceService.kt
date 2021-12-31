package io.github.skriptinsight.redux.lsp.services

import io.github.skriptinsight.redux.lsp.workspace.LspSkriptWorkspace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageClientAware
import org.eclipse.lsp4j.services.WorkspaceService
import kotlin.coroutines.CoroutineContext

class SkriptWorkspaceService(override var coroutineContext: CoroutineContext, val workspace: LspSkriptWorkspace) :
    WorkspaceService, LanguageClientAware, CoroutineScope {

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

    suspend fun onClientInitialized(client: LanguageClient) {
        client.createProgress(WorkDoneProgressCreateParams(Either.forLeft("sus"))).await()

        client.notifyProgress(
            ProgressParams(
                Either.forLeft("sus"),
                Either.forLeft(WorkDoneProgressBegin(
                ).apply {
                    title = "sus"
                    this.message = "Being sus is fun"
                })
            )
        )

        val configResult = client.configuration(ConfigurationParams(listOf(ConfigurationItem().apply {
            this.section = "skriptinsight"
        }))).await()

        workspace.logger.info("Successfully requested configuration from client")
    }

}
