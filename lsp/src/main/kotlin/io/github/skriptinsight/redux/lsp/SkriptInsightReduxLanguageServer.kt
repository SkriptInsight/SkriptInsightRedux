package io.github.skriptinsight.redux.lsp

import io.github.skriptinsight.redux.lsp.services.SkriptTextDocumentService
import io.github.skriptinsight.redux.lsp.services.SkriptWorkspaceService
import io.github.skriptinsight.redux.lsp.workspace.LspSkriptWorkspace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.services.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

class SkriptInsightReduxLanguageServer : LanguageServer, LanguageClientAware, CoroutineScope {
    override val coroutineContext: CoroutineContext
            = Dispatchers.Default + SupervisorJob()

    private val currentWorkspace = LspSkriptWorkspace(coroutineContext)

    private val textDocumentService = SkriptTextDocumentService(coroutineContext, currentWorkspace)
    private val workspaceService = SkriptWorkspaceService(coroutineContext, currentWorkspace)

    override fun initialize(params: InitializeParams): CompletableFuture<InitializeResult> {
        val capabilities = ServerCapabilities()
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Incremental)
        capabilities.workspace = WorkspaceServerCapabilities()
        capabilities.workspace.workspaceFolders = WorkspaceFoldersOptions()
        capabilities.workspace.workspaceFolders.setChangeNotifications(true)
        capabilities.setDocumentSymbolProvider(true)

        return completedFuture(InitializeResult(capabilities, ServerInfo("SkriptInsight")))
    }

    override fun shutdown(): CompletableFuture<Any> {
        return completedFuture(null)
    }

    override fun exit() {
        exitProcess(0)
    }

    override fun getTextDocumentService(): TextDocumentService {
        return textDocumentService
    }

    override fun getWorkspaceService(): WorkspaceService {
        return workspaceService
    }

    override fun initialized(params: InitializedParams) {

        launch {
            textDocumentService.onClientInitialized(client)
            workspaceService.onClientInitialized(client)
        }
    }

    lateinit var client: LanguageClient
    override fun connect(client: LanguageClient) {
        this.client = client
        currentWorkspace.client = client
        textDocumentService.connect(client)
        workspaceService.connect(client)
    }
}