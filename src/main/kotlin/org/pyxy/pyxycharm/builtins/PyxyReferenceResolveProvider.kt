package org.pyxy.pyxycharm.builtins

import com.intellij.psi.PsiManager
import com.jetbrains.python.psi.PyFile
import com.jetbrains.python.psi.PyQualifiedExpression
import com.jetbrains.python.psi.resolve.PyReferenceResolveProvider
import com.jetbrains.python.psi.resolve.RatedResolveResult
import com.jetbrains.python.psi.types.TypeEvalContext
import org.pyxy.pyxycharm.PyxyLanguageDialect

class PyxyReferenceResolveProvider : PyReferenceResolveProvider {
    override fun resolveName(expression: PyQualifiedExpression, context: TypeEvalContext): List<RatedResolveResult> {
        if (expression.containingFile.language !is PyxyLanguageDialect) {
            return emptyList()
        }
        val builtinsFile = PsiManager.getInstance(expression.project).findFile(
            PyxyBuiltins.PYXY_BUILTINS
        ) as? PyFile ?: return emptyList()
        return listOfNotNull(
            builtinsFile.findExportedName(expression.name)?.let {
                RatedResolveResult(RatedResolveResult.RATE_NORMAL, it)
            },
        )
    }
}