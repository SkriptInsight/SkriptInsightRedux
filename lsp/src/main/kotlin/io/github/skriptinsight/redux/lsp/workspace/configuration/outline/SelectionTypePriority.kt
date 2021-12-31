package io.github.skriptinsight.redux.lsp.workspace.configuration.outline

import com.google.gson.annotations.SerializedName

enum class SelectionTypePriority {
    @SerializedName("names")
    NAMES,
    @SerializedName("content")
    CONTENT,
    @SerializedName("comments")
    COMMENTS
}