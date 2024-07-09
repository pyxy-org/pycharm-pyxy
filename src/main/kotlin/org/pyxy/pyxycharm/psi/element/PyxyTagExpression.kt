package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.PyElementTypes
import com.jetbrains.python.psi.PyArgumentList
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.impl.PyCallExpressionImpl

class PyxyTagExpression(node: ASTNode) : PyCallExpressionImpl(node) {
    override fun getCallee(): PyExpression? {
        return findChildByType(PyElementTypes.REFERENCE_EXPRESSION)
    }

    override fun getArgumentList(): PyArgumentList? {
        return findChildByType(PyElementTypes.ARGUMENT_LIST)
    }

    override fun toString(): String {
        return "${this::class.simpleName}: ${callee?.name}"
    }
}