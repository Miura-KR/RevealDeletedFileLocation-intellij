package com.k.pmpstudy

import com.intellij.ide.projectView.ProjectView
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.VcsDataKeys
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowId
import com.intellij.openapi.wm.ToolWindowManager

/**
 * 「コミット」ツールウィンドウで削除されたファイルを右クリックしたときに表示される
 * 「Reveal Deleted File Location」アクション。
 *
 * 削除されたファイルが元々存在していたディレクトリを「プロジェクト」ビューで
 * フォーカスする。元のディレクトリ自体も削除されている場合は、存在している
 * 最も近い祖先ディレクトリまで遡ってフォーカスする。
 */
class RevealDeletedFileLocationAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null && getTargetDeletedChange(e) != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val change = getTargetDeletedChange(e) ?: return
        val deletedFilePath = change.beforeRevision?.file ?: return

        val targetDir = findNearestExistingDirectory(deletedFilePath.parentPath) ?: return

        // 「プロジェクト」ツールウィンドウが閉じている場合でも開いて選択できるように、
        // まずツールウィンドウを activate してから select を呼ぶ。
        val projectToolWindow = ToolWindowManager.getInstance(project).getToolWindow(ToolWindowId.PROJECT_VIEW)
        if (projectToolWindow != null) {
            projectToolWindow.activate({
                ProjectView.getInstance(project).select(null, targetDir, true)
            }, true)
        } else {
            ProjectView.getInstance(project).select(null, targetDir, true)
        }
    }

    /**
     * 選択中の Change がちょうど 1 件で、それが削除された変更であればそれを返す。
     * そうでなければ null を返す(= アクションを非表示にする)。
     */
    private fun getTargetDeletedChange(e: AnActionEvent): Change? {
        val changes = e.getData(VcsDataKeys.CHANGES) ?: return null
        if (changes.size != 1) return null
        val change = changes[0]
        return if (change.type == Change.Type.DELETED) change else null
    }

    /**
     * 指定された FilePath から祖先を辿り、VFS 上に存在している最初のディレクトリを返す。
     */
    private fun findNearestExistingDirectory(startPath: FilePath?): VirtualFile? {
        var current: FilePath? = startPath
        while (current != null) {
            val vFile = current.virtualFile
            if (vFile != null && vFile.isValid && vFile.isDirectory) {
                return vFile
            }
            current = current.parentPath
        }
        return null
    }
}
