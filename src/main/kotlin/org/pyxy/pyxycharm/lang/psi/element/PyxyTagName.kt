package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl
import org.pyxy.pyxycharm.lang.highlighter.PyxyAnnotatingVisitor

class PyxyTagName(node: ASTNode): PyReferenceExpressionImpl(node) {
    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyTagName(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }

    override fun toString(): String {
        return "PyxyTagObjectRef: $referencedName"
    }
}