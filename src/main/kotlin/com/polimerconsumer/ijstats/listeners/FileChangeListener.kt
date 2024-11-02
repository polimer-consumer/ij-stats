package com.polimerconsumer.ijstats.listeners

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.polimerconsumer.ijstats.MethodsParser
import com.polimerconsumer.ijstats.ui.MethodsPanel

class FileChangeListener(
    private val project: Project,
    private val methodsPanel: MethodsPanel
) : FileEditorManagerListener {

    override fun fileOpened(manager: FileEditorManager, file: VirtualFile) {
        refreshMethodOutline(file)
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        event.newFile?.let { refreshMethodOutline(it) }
    }

    private fun refreshMethodOutline(file: VirtualFile) {
        val psiFile = PsiManager.getInstance(project).findFile(file) ?: return
        methodsPanel.setPsiFile(psiFile)
        val methods = MethodsParser.extractMethodsFromFile(psiFile)
        methodsPanel.updateMethods(methods)
    }
}
