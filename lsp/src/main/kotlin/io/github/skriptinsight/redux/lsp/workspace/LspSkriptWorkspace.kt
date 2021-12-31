package io.github.skriptinsight.redux.lsp.workspace

import io.github.skriptinsight.redux.file.workspace.WorkspaceLogger
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import org.eclipse.lsp4j.services.LanguageClient

class LspSkriptWorkspace : SkriptWorkspace() {
    lateinit var client: LanguageClient

    override val logger: WorkspaceLogger = LspWorkspaceLogger(this)
}

