package com.k.pmpstudy

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.FilePath
import com.intellij.openapi.vcs.changes.Change
import com.intellij.openapi.vcs.vfs.ContentRevisionVirtualFile
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import com.intellij.ui.SimpleTextAttributes

class DeletedFileNode(
    project: Project,
    private val filePath: FilePath,
    private val change: Change,
    private val attachedDir: VirtualFile,
) : AbstractTreeNode<FilePath>(project, filePath) {

    override fun getChildren(): Collection<AbstractTreeNode<*>> = emptyList()

    override fun update(presentation: PresentationData) {
        val displayText = computeDisplayText()
        val icon = FileTypeManager.getInstance().getFileTypeByFileName(filePath.name).icon
        presentation.setIcon(icon)
        presentation.presentableText = displayText

        val attrs = when (change.type) {
            Change.Type.DELETED ->
                SimpleTextAttributes(SimpleTextAttributes.STYLE_STRIKEOUT, JBColor.GRAY)
            Change.Type.MOVED ->
                SimpleTextAttributes(SimpleTextAttributes.STYLE_ITALIC, JBColor.GRAY)
            else -> SimpleTextAttributes.GRAYED_ATTRIBUTES
        }
        presentation.addText(displayText, attrs)

        presentation.tooltip = when (change.type) {
            Change.Type.DELETED -> "Deleted: ${filePath.path}"
            Change.Type.MOVED -> {
                val after = change.afterRevision?.file?.path
                if (after != null) "Moved to: $after\nFrom: ${filePath.path}" else "Moved: ${filePath.path}"
            }
            else -> filePath.path
        }
    }

    private fun computeDisplayText(): String {
        val attachedPath = attachedDir.path
        val originalPath = filePath.path
        val prefix = if (attachedPath.endsWith("/")) attachedPath else "$attachedPath/"
        return if (originalPath.startsWith(prefix)) {
            originalPath.removePrefix(prefix)
        } else {
            filePath.name
        }
    }

    override fun canNavigate(): Boolean = change.beforeRevision != null
    override fun canNavigateToSource(): Boolean = canNavigate()

    override fun navigate(requestFocus: Boolean) {
        val project = project ?: return
        val before = change.beforeRevision ?: return
        val virtualFile = ContentRevisionVirtualFile.create(before)
        OpenFileDescriptor(project, virtualFile).navigate(requestFocus)
    }

    override fun getWeight(): Int = SORT_WEIGHT

    companion object {
        // Below regular files (which use the default 0 weight via PsiFileNode).
        private const val SORT_WEIGHT = 100
    }
}
