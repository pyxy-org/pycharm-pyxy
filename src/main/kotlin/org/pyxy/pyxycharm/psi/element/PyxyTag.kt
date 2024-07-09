package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyElementImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyTag(node: ASTNode) : PyElementImpl(node) {
    private val tagTokens = TokenSet.create(PyTokenTypes.LT, PyTokenTypes.GT, PyTokenTypes.DIV)

    fun getHighlightedTagNodes(): List<PsiElement> {
        return findChildrenByType(tagTokens)
    }

    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyTag(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }

    override fun toString(): String {
        return "${this::class.simpleName}: ${this.text}"
    }
}