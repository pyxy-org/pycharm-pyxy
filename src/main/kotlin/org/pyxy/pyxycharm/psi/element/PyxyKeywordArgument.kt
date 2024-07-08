package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyKeywordArgumentImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyKeywordArgument(node: ASTNode) : PyKeywordArgumentImpl(node) {
    override fun getKeywordNode(): ASTNode? {
        return node.findChildByType(PyxyElementTypes.TAG_KEYWORD_ARGUMENT_NAME)
    }

    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyAttribute(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }

    override fun toString(): String {
        return "${this::class.simpleName}: ${this.text}"
    }
}