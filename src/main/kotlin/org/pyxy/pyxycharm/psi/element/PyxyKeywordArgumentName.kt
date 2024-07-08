package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyStringLiteralExpressionImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyKeywordArgumentName(node: ASTNode) : PyStringLiteralExpressionImpl(node) {
    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyAttributeName(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }
}
