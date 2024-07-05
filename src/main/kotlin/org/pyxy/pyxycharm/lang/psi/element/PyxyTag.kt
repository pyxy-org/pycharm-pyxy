package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.PyArgumentList
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.impl.PyCallExpressionImpl

class PyxyTag(node: ASTNode): PyCallExpressionImpl(node) {
    override fun getCallee(): PyExpression? { return findChildByType(PyxyElementTypes.TAG_NAME) }

    override fun getArgumentList(): PyArgumentList? { return findChildByType(PyxyElementTypes.ARG_LIST) }

    override fun toString(): String {
        val callee = callee
        return "PyxyTag: ${if (callee == null) "null" else callee.name}"
    }
}