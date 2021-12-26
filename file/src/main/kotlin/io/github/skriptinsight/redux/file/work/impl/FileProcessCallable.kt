package io.github.skriptinsight.redux.file.work.impl

import io.github.skriptinsight.redux.file.work.FileProcess
import java.util.concurrent.Callable

internal class FileProcessCallable<F, R, C>(
    private val process: FileProcess<F, R, C>,
    private val file: F,
    private val lineNumber: Int,
    private val rawContent: String,
    private val context: C
) : Callable<R> {
    override fun call(): R? {
        return try {
            process.doWork(file, lineNumber, rawContent, context)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
}
