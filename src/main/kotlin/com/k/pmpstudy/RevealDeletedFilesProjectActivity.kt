package com.k.pmpstudy

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.vcs.changes.ChangeListListener

class RevealDeletedFilesProjectActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        project.messageBus.connect().subscribe(
            ChangeListListener.TOPIC,
            object : ChangeListListener {
                override fun changeListUpdateDone() {
                    val s = RevealDeletedFilesSettings.getInstance(project)
                    if (s.showDeleted || s.showMoved) {
                        requestProjectViewRefresh(project)
                    }
                }
            },
        )
    }
}
