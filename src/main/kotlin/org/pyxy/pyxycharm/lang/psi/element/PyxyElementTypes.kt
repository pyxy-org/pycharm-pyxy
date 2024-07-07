package org.pyxy.pyxycharm.lang.psi.element

import com.jetbrains.python.psi.PyElementType

object PyxyElementTypes {
    val TAG = PyElementType("TAG") { PyxyTagCall(it) }
    val TAG_NAME = PyElementType("TAG_NAME") { PyxyTagCallName(it) }
    val ARGUMENT_LIST = PyElementType("ARG_LIST") { PyxyArgumentList(it) }
    val KEYWORD_ARGUMENT = PyElementType("ARG_LIST") { PyxyKeywordArgument(it) }
    val KEYWORD_ARGUMENT_NAME = PyElementType("ATTR_NAME") { PyxyKeywordArgumentName(it) }
}
