package com.k.pmpstudy

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ui.RollbackChangesDialog

class RollbackDeletedFileAction : AnAction(), DumbAware {

    init {
        templatePresentation.icon = AllIcons.Actions.Rollback
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        val visible = e.project != null && collectChanges(e).isNotEmpty()
        e.presentation.isEnabledAndVisible = visible
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val changes = collectChanges(e)
        if (changes.isEmpty()) return
        RollbackChangesDialog.rollbackChanges(project, changes)
    }

    private fun collectChanges(e: AnActionEvent): List<Change> {
        val navs = e.getData(CommonDataKeys.NAVIGATABLE_ARRAY) ?: return emptyList()
        if (navs.isEmpty()) return emptyList()
        if (navs.any { it !is DeletedFileNode }) return emptyList()
        return navs.filterIsInstance<DeletedFileNode>().map { it.change }
    }
}
