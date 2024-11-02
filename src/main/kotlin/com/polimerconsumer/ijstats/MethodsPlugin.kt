package com.polimerconsumer.ijstats

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import javax.swing.JPanel
import javax.swing.JLabel

class MethodsToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val panel = JPanel()
        panel.add(JLabel("Method Outline Content"))
        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(panel, "Methods", false)
        contentManager.addContent(content)
    }
}

