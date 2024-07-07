package org.pyxy.pyxycharm.lang.psi.element

import com.intellij.lang.ASTNode
import com.jetbrains.python.psi.impl.PyKeywordArgumentImpl

class PyxyKeywordArgument(node: ASTNode): PyKeywordArgumentImpl(node) {
    override fun getKeywordNode(): ASTNode? {
        return node.findChildByType(PyxyElementTypes.KEYWORD_ARGUMENT_NAME)
    }
}