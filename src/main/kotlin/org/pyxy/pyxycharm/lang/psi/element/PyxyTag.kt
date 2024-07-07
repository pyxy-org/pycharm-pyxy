package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyElementImpl
import org.pyxy.pyxycharm.lang.highlighter.PyxyAnnotatingVisitor

class PyxyTag(node: ASTNode): PyElementImpl(node) {
    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyTag(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }
}