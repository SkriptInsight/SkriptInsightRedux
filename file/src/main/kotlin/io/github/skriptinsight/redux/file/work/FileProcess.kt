package io.github.skriptinsight.redux.file.work

import java.util.*

/**
 * Represents an abstract file process.
 *
 * @param F The type of the file to run the process on
 * @param R The return type of the process
 * @param C The context (or Unit) for the process
 */
abstract class FileProcess<F, R, C>(val id: UUID, val title: String, val description: String?) {
    abstract fun doWork(file: F, lineNumber: Int, rawContent: String, context: C): R
}