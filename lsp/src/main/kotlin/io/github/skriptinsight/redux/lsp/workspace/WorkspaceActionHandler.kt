package io.github.skriptinsight.redux.lsp.workspace

import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace
import java.net.URI

object WorkspaceActionHandler {

    fun onFileOpen(workspace: SkriptWorkspace, uri: String, text: String) {
        val file = SkriptFile.fromText(URI(uri), workspace, text)
        workspace.addFile(file)
    }

    fun onFileClose(workspace: SkriptWorkspace, uri: String) {
        workspace.removeFile(URI(uri))
    }

}