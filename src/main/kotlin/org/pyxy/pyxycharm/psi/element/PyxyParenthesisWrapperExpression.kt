package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.impl.PyParenthesizedExpressionImpl

class PyxyParenthesisWrapperExpression(node: ASTNode) : PyParenthesizedExpressionImpl(node) {
    override fun textContains(c: Char): Boolean {
        // TODO: HACK! This is the only decent way I've found to suppress PyRedundantParenthesesInspection
        if (c == '\n') return true
        return super.textContains(c)
    }
}