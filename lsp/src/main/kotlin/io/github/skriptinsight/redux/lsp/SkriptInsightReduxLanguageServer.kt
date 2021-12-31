package io.github.skriptinsight.redux.lsp

import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import io.github.skriptinsight.redux.lsp.services.SkriptTextDocumentService
import io.github.skriptinsight.redux.lsp.services.SkriptWorkspaceService
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.services.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.completedFuture
import kotlin.system.exitProcess

val currentWorkspace = SkriptWorkspace()

class SkriptInsightReduxLanguageServer : LanguageServer, LanguageClientAware {

    override fun initialize(params: InitializeParams): CompletableFuture<InitializeResult> {
        val capabilities = ServerCapabilities()
        capabilities.setTextDocumentSync(TextDocumentSyncKind.Incremental)
        capabilities.workspace = WorkspaceServerCapabilities()
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
        return SkriptTextDocumentService
    }

    override fun getWorkspaceService(): WorkspaceService {
        return SkriptWorkspaceService
    }

    override fun connect(client: LanguageClient) {
        currentWorkspace["client"] = client
        SkriptWorkspaceService.connect(client)
        SkriptTextDocumentService.connect(client)
    }
}