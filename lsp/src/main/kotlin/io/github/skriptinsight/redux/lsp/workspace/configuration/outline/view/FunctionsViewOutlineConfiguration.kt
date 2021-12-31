package io.github.skriptinsight.redux.lsp.workspace.configuration.outline.view

data class FunctionsViewOutlineConfiguration(
    val calls: BaseViewOutlineConfiguration = BaseViewOutlineConfiguration()
) : BaseViewOutlineConfiguration() {
    override fun toString(): String {
        return "FunctionsViewOutlineConfiguration(calls=$calls, enabled=$enabled)"
    }
}