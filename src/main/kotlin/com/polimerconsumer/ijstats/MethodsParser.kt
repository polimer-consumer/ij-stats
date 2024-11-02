package com.polimerconsumer.ijstats

import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtNamedFunction

object MethodsParser {
    fun extractMethodsFromFile(psiFile: PsiFile): List<MethodInfo> {
        val methods = mutableListOf<MethodInfo>()

        PsiTreeUtil.findChildrenOfType(psiFile, KtNamedFunction::class.java).forEach { function ->
            val methodName = function.name ?: "Unnamed"
            val returnType = function.typeReference?.text ?: "Unit"
            val modifierList = function.modifierList
            val accessModifier = when {
                modifierList?.hasModifier(KtTokens.PUBLIC_KEYWORD) == true -> "public"
                modifierList?.hasModifier(KtTokens.PRIVATE_KEYWORD) == true -> "private"
                modifierList?.hasModifier(KtTokens.PROTECTED_KEYWORD) == true -> "protected"
                else -> "internal"
            }
            val offset = function.textOffset

            val arguments = function.valueParameters.map { param ->
                val paramName = param.name ?: "Unnamed"
                val paramType = param.typeReference?.text ?: "Any"
                MethodArgument(paramName, paramType)
            }

            methods.add(MethodInfo(
                name = methodName,
                returnType = returnType,
                accessModifier = accessModifier,
                arguments = arguments,
                offset = offset
            ))
        }
        return methods
    }
}

data class MethodInfo(
    val name: String,
    val returnType: String,
    val accessModifier: String,
    val arguments: List<MethodArgument>,
    val offset: Int
)

data class MethodArgument(
    val name: String,
    val type: String
)
