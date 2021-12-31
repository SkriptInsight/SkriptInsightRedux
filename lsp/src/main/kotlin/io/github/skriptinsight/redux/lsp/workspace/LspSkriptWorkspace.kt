package io.github.skriptinsight.redux.lsp.workspace

import com.google.gson.Gson
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import io.github.skriptinsight.redux.file.workspace.WorkspaceLogger
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import io.github.skriptinsight.redux.lsp.workspace.configuration.LspConfiguration
import io.github.skriptinsight.redux.lsp.workspace.process.WorkReportingFileProcessCallable
import io.github.skriptinsight.redux.lsp.workspace.work.LspWork
import kotlinx.coroutines.CoroutineScope
import org.eclipse.lsp4j.services.LanguageClient
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.CoroutineContext

class LspSkriptWorkspace(override val coroutineContext: CoroutineContext) : SkriptWorkspace(), CoroutineScope {
    lateinit var client: LanguageClient
    private var isLspClientInitialized = false
    private val tasksStack = ArrayDeque<() -> Unit>()
    override val logger: WorkspaceLogger = LspWorkspaceLogger(this)
    val gson = Gson()
    var configuration: LspConfiguration = LspConfiguration()

    override fun <R> runProcess(skriptFile: SkriptFile, process: SkriptFileProcess<R>): List<R> {
        val work = LspWork(client)
        work.reportBeginWork(process.title, process.description)
        val nodesCount = skriptFile.nodes.maxOfOrNull { it.key } ?: 0
        val finishedCount = AtomicInteger(0)
        val map = skriptFile.nodes.map {
            WorkReportingFileProcessCallable(
                process, skriptFile, it.key, it.value.rawContent, it.value, work,
                nodesCount, finishedCount
            )
        }
        val result = ForkJoinPool.commonPool().invokeAll(map).map { it.get() }
        work.reportWorkDone(process.description)
        return result
    }

    override fun delayUntilReadyIfNeeded(code: () -> Unit) {
        if (!isLspClientInitialized) tasksStack.addLast(code) else code()
    }

    fun onClientInitialized() {
        isLspClientInitialized = true
        while (tasksStack.isNotEmpty()) {
            tasksStack.removeFirst().invoke()
        }
    }
}

