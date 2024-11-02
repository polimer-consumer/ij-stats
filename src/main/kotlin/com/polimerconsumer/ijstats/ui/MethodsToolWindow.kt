package com.polimerconsumer.ijstats.ui

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.polimerconsumer.ijstats.listeners.FileChangeListener

class MethodsWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val methodsPanel = MethodsPanel(project)

        val content = contentManager.factory.createContent(methodsPanel, "Methods", false)
        contentManager.addContent(content)

        FileEditorManager.getInstance(project).addFileEditorManagerListener(
            FileChangeListener(project, methodsPanel)
        )
    }
}
