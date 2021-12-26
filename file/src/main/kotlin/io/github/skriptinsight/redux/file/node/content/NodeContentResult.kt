package io.github.skriptinsight.redux.file.node.content

import io.github.skriptinsight.redux.file.location.Range

data class NodeContentResult(
    val content: String,
    val contentRange: Range,
    val comment: String,
    val commentRange: Range,
    val unIndentedRawContent: String,
    val normalizedIndentCount: Int,
    val rawIndentCount: Int
)