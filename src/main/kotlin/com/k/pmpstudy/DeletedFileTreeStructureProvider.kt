package com.k.pmpstudy

import com.intellij.ide.projectView.TreeStructureProvider
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.ProjectLevelVcsManager
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.changes.ChangeListManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory

class DeletedFileTreeStructureProvider : TreeStructureProvider {

    override fun modify(
        parent: AbstractTreeNode<*>,
        children: MutableCollection<AbstractTreeNode<*>>,
        settings: ViewSettings,
    ): MutableCollection<AbstractTreeNode<*>> {
        val project = parent.project ?: return children
        val parentDir = (parent.value as? PsiDirectory)?.virtualFile ?: return children
        if (!parentDir.isValid || !parentDir.isDirectory) return children

        val s = RevealDeletedFilesSettings.getInstance(project)
        if (!s.showDeleted && !s.showMoved) return children
        if (!ProjectLevelVcsManager.getInstance(project).hasActiveVcss()) return children

        val phantoms = collectPhantomsForDirectory(project, parentDir, s)
        if (phantoms.isNotEmpty()) {
            children.addAll(phantoms)
        }
        return children
    }

    private fun collectPhantomsForDirectory(
        project: Project,
        parentDir: VirtualFile,
        settings: RevealDeletedFilesSettings,
    ): List<DeletedFileNode> {
        val results = mutableListOf<DeletedFileNode>()
        val seenPaths = HashSet<String>()
        val changes = ChangeListManager.getInstance(project).allChanges
        for (change in changes) {
            val include = when (change.type) {
                Change.Type.DELETED -> settings.showDeleted
                Change.Type.MOVED -> settings.showMoved
                else -> false
            }
            if (!include) continue
            val before = change.beforeRevision?.file ?: continue
            val phantomParent = before.parentPath ?: continue
            val nearestExisting = findNearestExistingDirectory(phantomParent) ?: continue
            if (!nearestExisting.equals(parentDir)) continue
            if (!seenPaths.add(before.path)) continue
            results.add(DeletedFileNode(project, before, change, parentDir))
        }
        return results
    }

    private fun findNearestExistingDirectory(start: FilePath): VirtualFile? {
        var current: FilePath? = start
        while (current != null) {
            val v = current.virtualFile
            if (v != null && v.isValid && v.isDirectory) return v
            current = current.parentPath
        }
        return null
    }
}
