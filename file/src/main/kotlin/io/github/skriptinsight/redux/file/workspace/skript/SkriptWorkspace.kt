package io.github.skriptinsight.redux.file.workspace.skript

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.work.SkriptFileProcess
import io.github.skriptinsight.redux.file.work.impl.FileProcessCallable
import io.github.skriptinsight.redux.file.workspace.BaseWorkspace
import io.github.skriptinsight.redux.file.workspace.WorkspaceLanguage
import io.github.skriptinsight.redux.file.workspace.providers.SectionParser
import java.util.concurrent.ForkJoinPool

open class SkriptWorkspace : BaseWorkspace() {
    override val language: WorkspaceLanguage
        get() = WorkspaceLanguage.SKRIPT

    override val sectionProviders: List<SectionParser> = listOf(
        FunctionSectionParser
    )

    override fun <R> runProcess(
        skriptFile: SkriptFile,
        process: SkriptFileProcess<R>,
        startIndex: Int,
        endIndex: Int
    ): List<R> {
        val map = skriptFile.nodes.filterKeys { it in startIndex..endIndex }.map {
            FileProcessCallable(process, skriptFile, it.key, it.value.rawContent, it.value)
        }
        return ForkJoinPool.commonPool().invokeAll(map).map { it.get() }
    }
}