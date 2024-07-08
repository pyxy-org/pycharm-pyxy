package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyElementImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyTagCData(node: ASTNode): PyElementImpl(node) {
    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyCData(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }

    override fun toString(): String {
        return "PyxyTagCData"
    }
}