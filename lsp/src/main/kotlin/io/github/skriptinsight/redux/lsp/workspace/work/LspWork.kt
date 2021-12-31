package io.github.skriptinsight.redux.lsp.workspace.work

import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.LanguageClient
import java.util.*

data class LspWork(val client: LanguageClient, val id: UUID = UUID.randomUUID()) {
    private val processIdEither = Either.forLeft<String, Int>(id.toString())

    init {
        client.createProgress(WorkDoneProgressCreateParams(processIdEither)).thenAccept{
        }
    }

    fun reportBeginWork(title: String, message: String?) {
        client.notifyProgress(
            ProgressParams(
                processIdEither,
                Either.forLeft(WorkDoneProgressBegin().apply {
                    this.title = title
                    this.message = message
                })
            )
        )
    }

    fun reportWorkProgress(message: String?, percentage: Int) {
        // Check if the percentage is between 0 and 100
        if (percentage < 0 || percentage > 100) {
            throw IllegalArgumentException("Percentage must be between 0 and 100")
        }

        client.notifyProgress(
            ProgressParams(
                processIdEither,
                Either.forLeft(WorkDoneProgressReport().apply {
                    this.message = message
                    this.percentage = percentage
                })
            )
        )
    }

    fun reportWorkDone(message: String?) {
        client.notifyProgress(
            ProgressParams(
                processIdEither,
                Either.forLeft(WorkDoneProgressEnd().apply {
                    this.message = message
                })
            )
        )
    }
}