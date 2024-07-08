package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.PyElementTypes
import com.jetbrains.python.psi.PyArgumentList
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyExpression
import com.jetbrains.python.psi.impl.PyCallExpressionImpl
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyTagExpression(node: ASTNode): PyCallExpressionImpl(node) {
    override fun getCallee(): PyExpression? {
        return findChildByType(PyxyElementTypes.TAG_NAME)
    }

    override fun getArgumentList(): PyArgumentList? {
        return findChildByType(PyElementTypes.ARGUMENT_LIST)
    }

    override fun toString(): String {
        val callee = callee
        return "PyxyTag: ${if (callee == null) "null" else callee.name}"
    }
}