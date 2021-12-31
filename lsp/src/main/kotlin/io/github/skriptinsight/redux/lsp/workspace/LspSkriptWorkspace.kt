package io.github.skriptinsight.redux.lsp.workspace

import com.google.gson.Gson
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import io.github.skriptinsight.redux.file.workspace.WorkspaceLogger
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import io.github.skriptinsight.redux.lsp.workspace.process.SuspendSkriptFileProcess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.eclipse.lsp4j.services.LanguageClient
import kotlin.coroutines.CoroutineContext

class LspSkriptWorkspace(override val coroutineContext: CoroutineContext) : SkriptWorkspace(), CoroutineScope {
    lateinit var client: LanguageClient

    override val logger: WorkspaceLogger = LspWorkspaceLogger(this)

    val gson = Gson()

    override fun <R> runProcess(skriptFile: SkriptFile, process: SkriptFileProcess<R>): List<R> {
        if (process !is SuspendSkriptFileProcess<*>) {
            return super.runProcess(skriptFile, process)
        }

        with(skriptFile) {
            val lspWorkspace = workspace as? LspSkriptWorkspace ?: throw IllegalStateException("Workspace is not LspSkriptWorkspace")

            return runBlocking {
               nodes.entries.asFlow().map {
                   withContext(lspWorkspace.coroutineContext) {
                       process.doWorkSuspend(skriptFile, it.key, it.value.rawContent, it.value) as R
                   }
               }.toList()
            }
        }
    }
}

