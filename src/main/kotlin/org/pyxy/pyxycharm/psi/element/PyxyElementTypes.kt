package org.pyxy.pyxycharm.psi.element

import com.jetbrains.python.psi.PyElementType

object PyxyElementTypes {
    val TAG_EXPRESSION = PyElementType("TAG_EXPRESSION") {
        PyxyTagExpression(it)
    }
    val TAG_NAME = PyElementType("TAG_NAME") {
        PyxyTagName(it)
    }
    val TAG_KEYWORD_ARGUMENT_EXPRESSION = PyElementType("TAG_KEYWORD_ARGUMENT_EXPRESSION") {
        PyxyKeywordArgument(it)
    }
    val TAG_KEYWORD_ARGUMENT_NAME = PyElementType("TAG_KEYWORD_ARGUMENT_NAME") {
        PyxyKeywordArgumentName(it)
    }
    val TAG_CDATA = PyElementType("TAG_CDATA") {
        PyxyTagCData(it)
    }
}
