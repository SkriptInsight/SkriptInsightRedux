package io.github.skriptinsight.redux.lsp.services

import io.github.skriptinsight.redux.core.event.bus.EventBus
import io.github.skriptinsight.redux.file.analysis.event.SkriptFileInspectionResultsEvent
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.node.impl.EmptySkriptNode
import io.github.skriptinsight.redux.file.node.impl.skript.function.FunctionSectionSkriptNode
import io.github.skriptinsight.redux.lsp.extensions.toLspDiagnostic
import io.github.skriptinsight.redux.lsp.extensions.toLspRange
import io.github.skriptinsight.redux.lsp.workspace.LspSkriptWorkspace
import io.github.skriptinsight.redux.lsp.workspace.WorkspaceActionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.LanguageClient
import org.eclipse.lsp4j.services.LanguageClientAware
import org.eclipse.lsp4j.services.TextDocumentService
import java.net.URI
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

class SkriptTextDocumentService(override var coroutineContext: CoroutineContext, val workspace: LspSkriptWorkspace) :
    TextDocumentService, LanguageClientAware,
    CoroutineScope {

    lateinit var client: LanguageClient

    override fun connect(client: LanguageClient) {
        this.client = client
    }

    override fun documentSymbol(params: DocumentSymbolParams): CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> =
        future {
            val file = workspace.getFile(URI(params.textDocument.uri))
                ?: return@future emptyList()

            val result = file.rootNodes.mapNotNull { nodeToDocumentSymbol(it) }
            return@future result.map { Either.forRight(it) }
        }

    private fun nodeToDocumentSymbol(
        node: AbstractSkriptNode,
    ): DocumentSymbol? {
        if (node is EmptySkriptNode) return null
        var name = node.content.takeIf { it.isNotBlank() } ?: return null
        var detail = ""
        if (node is FunctionSectionSkriptNode) {
            name = node.signature?.name ?: return null
            detail = "(${node.signature?.parameters?.joinToString()})"
        }
        val contentRange = node.contentRange.toLspRange()
        val kind = if (node is FunctionSectionSkriptNode) SymbolKind.Function else SymbolKind.Event

        return DocumentSymbol(
            name,
            kind,
            contentRange,
            contentRange,
            detail,
            node.children?.mapNotNull { nodeToDocumentSymbol(it) } ?: emptyList())
    }

    override fun didOpen(params: DidOpenTextDocumentParams) {
        WorkspaceActionHandler.onFileOpen(workspace, params.textDocument.uri, params.textDocument.text)
    }

    override fun didChange(params: DidChangeTextDocumentParams) {
        WorkspaceActionHandler.onFileChange(workspace, params.textDocument.uri, params.contentChanges)
    }

    override fun didClose(params: DidCloseTextDocumentParams) {
        WorkspaceActionHandler.onFileClose(workspace, params.textDocument.uri)
    }

    override fun didSave(params: DidSaveTextDocumentParams) {
        workspace.logger.info("User saved document ${params.textDocument.uri}")
    }

    suspend fun onClientInitialized(client: LanguageClient) {
        registerInspectionResultsHandler()
    }

    private fun registerInspectionResultsHandler() {
        EventBus.register<SkriptFileInspectionResultsEvent> {
            client.publishDiagnostics(
                PublishDiagnosticsParams(
                    it.file.url.toString(),
                    it.diagnostics.map { diagnostic ->
                        diagnostic.toLspDiagnostic()
                    }
                )
            )
        }
    }
}