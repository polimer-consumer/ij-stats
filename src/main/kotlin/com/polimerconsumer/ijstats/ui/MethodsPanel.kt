package com.polimerconsumer.ijstats.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.editor.LogicalPosition
import com.intellij.openapi.editor.ScrollType
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.polimerconsumer.ijstats.MethodInfo
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.JScrollPane
import javax.swing.event.HyperlinkEvent
import java.awt.BorderLayout

class MethodsPanel(private val project: Project) : JPanel() {

    private val methodTextPane = JTextPane()
    private var currentPsiFile: PsiFile? = null

    init {
        layout = BorderLayout()
        methodTextPane.contentType = "text/html"
        methodTextPane.isEditable = false
        methodTextPane.addHyperlinkListener { event ->
            if (event.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                val methodOffset = event.description.toIntOrNull()
                if (methodOffset != null) {
                    navigateToMethod(methodOffset)
                }
            }
        }
        add(JScrollPane(methodTextPane), BorderLayout.CENTER)
    }

    fun updateMethods(methods: List<MethodInfo>) {
        val htmlContent = buildHtmlContent(methods)
        methodTextPane.text = htmlContent
    }

    private fun buildHtmlContent(methods: List<MethodInfo>): String {
        val htmlBuilder = StringBuilder("<html><body style='font-family:monospace; color:#333;'>")

        for (method in methods) {
            val modifierStyle = "color:#888; font-weight:bold;"
            val methodNameStyle = "color:#6f42c1; font-weight:bold;"
            val argumentStyle = "color:#e36209;"
            val typeStyle = "color:#f1e05a;"
            val plainTextStyle = "color:#ffffff;"
            val methodOffset = method.offset

            htmlBuilder.append("<p>")
            htmlBuilder.append("<span style='$modifierStyle'>${method.accessModifier}</span> ")
            htmlBuilder.append("<a href='$methodOffset' style='$methodNameStyle'>${method.name}</a>")

            if (method.arguments.isNotEmpty()) {
                htmlBuilder.append("<span style='$plainTextStyle'>(</span>")
                method.arguments.forEachIndexed { index, argument ->
                    htmlBuilder.append("<span style='$argumentStyle'>${argument.name}</span>")
                    htmlBuilder.append("<span style='$plainTextStyle'>: </span>")
                    htmlBuilder.append("<span style='$typeStyle'>${argument.type}</span>")
                    if (index < method.arguments.size - 1) {
                        htmlBuilder.append("<span style='$plainTextStyle'>, </span>")
                    }
                }
                htmlBuilder.append("<span style='$plainTextStyle'>)</span>")
            }

            htmlBuilder.append("<span style='$plainTextStyle'>: </span>")
            htmlBuilder.append("<span style='$typeStyle'>${method.returnType}</span>")

            htmlBuilder.append("</p>")
        }

        htmlBuilder.append("</body></html>")
        return htmlBuilder.toString()
    }




    fun setPsiFile(psiFile: PsiFile) {
        currentPsiFile = psiFile
    }

    private fun navigateToMethod(offset: Int) {
        currentPsiFile?.let { psiFile ->
            val editor = FileEditorManager.getInstance(project).selectedTextEditor ?: return
            val document = PsiDocumentManager.getInstance(project).getDocument(psiFile) ?: return
            val lineNumber = document.getLineNumber(offset)
            val lineStartOffset = document.getLineStartOffset(lineNumber)
            val columnNumber = offset - lineStartOffset

            editor.caretModel.moveToLogicalPosition(LogicalPosition(lineNumber, columnNumber))
            editor.scrollingModel.scrollToCaret(ScrollType.CENTER_UP)
        }
    }
}
