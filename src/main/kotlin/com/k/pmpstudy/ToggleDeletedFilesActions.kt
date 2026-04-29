package com.k.pmpstudy

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.DumbAware

class ToggleShowDeletedFilesAction : ToggleAction(), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        super.update(e)
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null &&
            com.intellij.openapi.vcs.ProjectLevelVcsManager.getInstance(project).hasActiveVcss()
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        val project = e.project ?: return false
        return RevealDeletedFilesSettings.getInstance(project).showDeleted
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        val project = e.project ?: return
        RevealDeletedFilesSettings.getInstance(project).showDeleted = state
    }
}

class ToggleShowMovedFilesAction : ToggleAction(), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        super.update(e)
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null &&
            com.intellij.openapi.vcs.ProjectLevelVcsManager.getInstance(project).hasActiveVcss()
    }

    override fun isSelected(e: AnActionEvent): Boolean {
        val project = e.project ?: return false
        return RevealDeletedFilesSettings.getInstance(project).showMoved
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        val project = e.project ?: return
        RevealDeletedFilesSettings.getInstance(project).showMoved = state
    }
}
