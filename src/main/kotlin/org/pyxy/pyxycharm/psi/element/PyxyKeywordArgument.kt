package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyElementTypes
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.impl.PyKeywordArgumentImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyKeywordArgument(node: ASTNode) : PyKeywordArgumentImpl(node) {
    private val stringElements = TokenSet.create(
        PyElementTypes.STRING_LITERAL_EXPRESSION,
        PyElementTypes.FSTRING_NODE
    )

    private val braceTokens = TokenSet.create(PyTokenTypes.LBRACE, PyTokenTypes.RBRACE)

    fun getHighlightedValueNodes(): List<PsiElement> {
        val result = ArrayList<PsiElement>()
        result.add(findChildByType(PyTokenTypes.EQ)!!)

        val stringChild = findChildByFilter(stringElements)
        if (stringChild != null) {
            result.add(stringChild)
        }

        return result
    }

    fun getHighlightedBraceNodes(): List<PsiElement> {
        return findChildrenByType(braceTokens)
    }

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