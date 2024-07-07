package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl


class PyxyTagCallName(node: ASTNode): PyReferenceExpressionImpl(node) {
    override fun toString(): String {
        return "PyxyTagObjectRef: $referencedName"
    }
}