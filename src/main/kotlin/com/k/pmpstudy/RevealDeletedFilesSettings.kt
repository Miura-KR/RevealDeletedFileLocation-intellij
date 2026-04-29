package com.k.pmpstudy

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
@State(
    name = "RevealDeletedFilesSettings",
    storages = [Storage("revealDeletedFiles.xml")],
)
class RevealDeletedFilesSettings(private val project: Project) :
    PersistentStateComponent<RevealDeletedFilesSettings.State> {

    data class State(
        var showDeleted: Boolean = true,
        var showMoved: Boolean = true,
    )

    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(s: State) {
        state = s
    }

    var showDeleted: Boolean
        get() = state.showDeleted
        set(value) {
            if (state.showDeleted != value) {
                state.showDeleted = value
                requestProjectViewRefresh(project)
            }
        }

    var showMoved: Boolean
        get() = state.showMoved
        set(value) {
            if (state.showMoved != value) {
                state.showMoved = value
                requestProjectViewRefresh(project)
            }
        }

    companion object {
        fun getInstance(project: Project): RevealDeletedFilesSettings = project.service()
    }
}

internal fun requestProjectViewRefresh(project: Project) {
    ApplicationManager.getApplication().invokeLater(
        Runnable {
            if (project.isDisposed) return@Runnable
            ProjectView.getInstance(project).currentProjectViewPane?.updateFromRoot(true)
        },
        ModalityState.nonModal(),
        project.disposed,
    )
}
