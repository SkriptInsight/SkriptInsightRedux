package io.github.skriptinsight.redux.lsp.workspace.configuration.outline

import io.github.skriptinsight.redux.lsp.workspace.configuration.outline.view.ViewOutlineConfiguration

data class OutlineConfiguration(
    val general: GeneralOutlineConfiguration = GeneralOutlineConfiguration(),
    val view: ViewOutlineConfiguration = ViewOutlineConfiguration()
)