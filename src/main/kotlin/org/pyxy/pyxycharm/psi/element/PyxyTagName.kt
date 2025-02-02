package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyTagName(node: ASTNode) : PyReferenceExpressionImpl(node) {
    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyTagName(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }

    override fun toString(): String {
        return "${this::class.simpleName}: $text"
    }
}