package io.github.skriptinsight.redux.lsp.workspace.process

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.work.FileProcess
import io.github.skriptinsight.redux.file.work.impl.FileProcessCallable
import io.github.skriptinsight.redux.lsp.workspace.work.LspWork
import java.util.concurrent.atomic.AtomicInteger

class WorkReportingFileProcessCallable<R, C>(
    process: FileProcess<SkriptFile, R, C>,
    file: SkriptFile, lineNumber: Int, rawContent: String, context: C, val work: LspWork, val nodesCount: Int, val finishedNodeCount: AtomicInteger
) : FileProcessCallable<SkriptFile, R, C>(process, file, lineNumber, rawContent, context) {
    override fun call(): R? {
        work.reportWorkProgress(
            "${process.description} (${finishedNodeCount.get() + 1}/$nodesCount)",
            (finishedNodeCount.get() / nodesCount.toDouble() * 100.0).toInt()
        )
        return super.call().also { finishedNodeCount.incrementAndGet() }
    }
}