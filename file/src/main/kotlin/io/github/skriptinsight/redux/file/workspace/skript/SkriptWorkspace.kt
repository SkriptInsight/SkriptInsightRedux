package io.github.skriptinsight.redux.file.workspace.skript

import io.github.skriptinsight.redux.file.workspace.BaseWorkspace
import io.github.skriptinsight.redux.file.workspace.WorkspaceLanguage

class SkriptWorkspace : BaseWorkspace() {
    override val language: WorkspaceLanguage
        get() = WorkspaceLanguage.SKRIPT
}