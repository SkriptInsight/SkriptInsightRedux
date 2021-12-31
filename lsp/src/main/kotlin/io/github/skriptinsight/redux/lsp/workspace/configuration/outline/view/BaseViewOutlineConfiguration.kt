package io.github.skriptinsight.redux.lsp.workspace.configuration.outline.view

open class BaseViewOutlineConfiguration(
    val enabled: Boolean = false
) {
    override fun toString(): String {
        return "BaseViewOutlineConfiguration(enabled=$enabled)"
    }
}