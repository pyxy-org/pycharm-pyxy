package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.PyElementTypes
import com.jetbrains.python.psi.PyArgumentList
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.impl.PyCallExpressionImpl

class PyxyTagExpression(node: ASTNode) : PyCallExpressionImpl(node) {
    override fun getCallee(): PyExpression? {
        val openingTag = findChildByType<PyxyTag>(PyxyElementTypes.TAG) ?: return null
        val tagName = openingTag.node.findChildByType(PyxyElementTypes.TAG_NAME) ?: return null
        return tagName.psi as PyExpression
    }

    override fun getArgumentList(): PyArgumentList? {
        val openingTag = findChildByType<PyxyTag>(PyxyElementTypes.TAG) ?: return null
        val tagName = openingTag.node.findChildByType(PyElementTypes.ARGUMENT_LIST) ?: return null
        return tagName.psi as PyArgumentList
    }

    override fun toString(): String {
        return "${this::class.simpleName}: ${callee?.name}"
    }
}