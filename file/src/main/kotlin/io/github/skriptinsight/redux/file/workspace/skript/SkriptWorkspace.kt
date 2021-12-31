package io.github.skriptinsight.redux.file.workspace.skript

import io.github.skriptinsight.redux.file.workspace.BaseWorkspace
import io.github.skriptinsight.redux.file.workspace.WorkspaceLanguage
import io.github.skriptinsight.redux.file.workspace.providers.SectionParser

open class SkriptWorkspace : BaseWorkspace() {
    override val language: WorkspaceLanguage
        get() = WorkspaceLanguage.SKRIPT

    override val sectionProviders: List<SectionParser> = listOf(
        FunctionSectionParser
    )
}