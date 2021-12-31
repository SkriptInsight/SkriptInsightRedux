package io.github.skriptinsight.redux.lsp.workspace.configuration

import io.github.skriptinsight.redux.lsp.workspace.configuration.outline.OutlineConfiguration
import io.github.skriptinsight.redux.lsp.workspace.configuration.styling.StylingConfiguration

data class LspConfiguration(
    val styling: StylingConfiguration = StylingConfiguration(),
    val outline: OutlineConfiguration = OutlineConfiguration()
)


