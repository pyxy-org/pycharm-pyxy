package org.pyxy.pyxycharm.builtins

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiManager
import com.intellij.util.ProcessingContext
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.psi.PyFile
import org.pyxy.pyxycharm.PyxyLanguageDialect

class PyxyCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement().withElementType(PyTokenTypes.IDENTIFIER),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    result: CompletionResultSet
                ) {
                    val element = parameters.position
                    if (element.containingFile.language !is PyxyLanguageDialect) {
                        return
                    }
                    val psiManager = PsiManager.getInstance(element.project)
                    listOfNotNull(
                        psiManager.findFile(PYXY_BUILTINS) as? PyFile,
                    ).forEach { file ->
                        file.topLevelClasses.forEach {
                            result.addElement(LookupElementBuilder.create(it))
                        }
                        file.topLevelFunctions.forEach {
                            result.addElement(LookupElementBuilder.create(it))
                        }
                    }
                }
            })
    }
}