package io.github.skriptinsight.redux.lsp.services

import com.google.gson.JsonObject
import io.github.skriptinsight.redux.lsp.workspace.LspSkriptWorkspace
import io.github.skriptinsight.redux.lsp.workspace.configuration.LspConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.await
import org.eclipse.lsp4j.ConfigurationItem
import org.eclipse.lsp4j.ConfigurationParams
import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.DidChangeWatchedFilesParams
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
        workspace.onClientInitialized()

        val configResult = client.configuration(ConfigurationParams(listOf(ConfigurationItem().apply {
            this.section = "skriptinsight"
        }))).await()

        workspace.logger.info("Successfully requested configuration from client")

        val configJsonObject = configResult[0] as? JsonObject ?: return

        val configuration = workspace.gson.fromJson(configJsonObject, LspConfiguration::class.java)

        workspace.logger.info("Successfully parsed configuration from client")

        workspace.configuration = configuration
    }

}
