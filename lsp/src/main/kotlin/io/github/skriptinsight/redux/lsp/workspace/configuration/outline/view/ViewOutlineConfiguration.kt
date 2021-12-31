package io.github.skriptinsight.redux.lsp.workspace.configuration.outline.view

data class ViewOutlineConfiguration(
    val functions: BaseViewOutlineConfiguration = BaseViewOutlineConfiguration(),
    val variables: BaseViewOutlineConfiguration = BaseViewOutlineConfiguration(),
    val events: BaseViewOutlineConfiguration = BaseViewOutlineConfiguration()
)