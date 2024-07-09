package org.pyxy.pyxycharm.builtins

import com.jetbrains.python.psi.PyQualifiedExpression
import com.jetbrains.python.psi.resolve.PyReferenceResolveProvider
import com.jetbrains.python.psi.resolve.RatedResolveResult
import com.jetbrains.python.psi.stubs.PyModuleNameIndex
import com.jetbrains.python.psi.types.TypeEvalContext
import org.pyxy.pyxycharm.PyxyLanguageDialect
import org.pyxy.pyxycharm.psi.element.PyxyTagName

class PyxyReferenceResolveProvider : PyReferenceResolveProvider {
    override fun resolveName(expression: PyQualifiedExpression, context: TypeEvalContext): List<RatedResolveResult> {
        if (expression.containingFile.language !is PyxyLanguageDialect) {
            return emptyList()
        }
        if (expression !is PyxyTagName) {
            return emptyList()
        }
//        println("resolveName with PyxyTagName: ${expression.text}")
//        println(expression.getReference().javaClass.name)

        // Lookup htpy module
        val matchingModules = PyModuleNameIndex.findByShortName("htpy", expression.project, expression.resolveScope)
        if (matchingModules.isEmpty()) {
            return emptyList()
        }
        val htpyModule = matchingModules[0]

        // Lookup expression name in htpy
        htpyModule.findExportedName(expression.name)
        return listOfNotNull(
            htpyModule.findExportedName(expression.name)?.let {
                RatedResolveResult(RatedResolveResult.RATE_NORMAL, it)
            },
        )
    }
}