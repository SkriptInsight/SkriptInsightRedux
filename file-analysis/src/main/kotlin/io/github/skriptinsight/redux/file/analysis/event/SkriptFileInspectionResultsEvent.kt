package io.github.skriptinsight.redux.file.analysis.event

import io.github.skriptinsight.redux.core.event.Event
import io.github.skriptinsight.redux.file.SkriptFile
import io.github.skriptinsight.redux.file.analysis.inspection.diagnostic.model.SkriptDiagnostic
import io.github.skriptinsight.redux.file.workspace.skript.SkriptWorkspace

data class SkriptFileInspectionResultsEvent(
    val file: SkriptFile, val workspace: SkriptWorkspace, val diagnostics: List<SkriptDiagnostic>
) : Event