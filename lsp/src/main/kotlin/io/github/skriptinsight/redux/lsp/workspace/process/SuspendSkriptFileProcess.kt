package io.github.skriptinsight.redux.lsp.workspace.process

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.node.AbstractSkriptNode
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import kotlinx.coroutines.runBlocking

abstract class SuspendSkriptFileProcess<R>(title: String, description: String?) : SkriptFileProcess<R>(title, description) {
    override fun doWork(file: SkriptFile, lineNumber: Int, rawContent: String, context: AbstractSkriptNode): R {
        return runBlocking {
            doWorkSuspend(file, lineNumber, rawContent, context)
        }
    }

    abstract suspend fun doWorkSuspend(file: SkriptFile, lineNumber: Int, rawContent: String, context: AbstractSkriptNode): R
}