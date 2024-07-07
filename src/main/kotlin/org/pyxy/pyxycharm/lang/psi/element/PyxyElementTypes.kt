package org.pyxy.pyxycharm.lang.psi.element

import com.jetbrains.python.psi.PyElementType

object PyxyElementTypes {
    val TAG_EXPRESSION = PyElementType("TAG_EXPRESSION") { PyxyTagExpression(it) }
    val TAG_NAME = PyElementType("TAG_NAME") { PyxyTagRef(it) }
    val TAG_KEYWORD_ARGUMENT_EXPRESSION = PyElementType("TAG_KEYWORD_ARGUMENT_EXPRESSION") { PyxyKeywordArgument(it) }
    val TAG_KEYWORD_ARGUMENT_NAME = PyElementType("TAG_KEYWORD_ARGUMENT_NAME") { PyxyKeywordArgumentName(it) }
}
