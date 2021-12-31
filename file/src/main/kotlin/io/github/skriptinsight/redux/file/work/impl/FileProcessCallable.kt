package io.github.skriptinsight.redux.file.work.impl

import io.github.skriptinsight.redux.file.work.FileProcess
import java.util.concurrent.Callable

open class FileProcessCallable<F, R, C>(
    protected val process: FileProcess<F, R, C>,
    protected val file: F,
    protected val lineNumber: Int,
    protected val rawContent: String,
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
