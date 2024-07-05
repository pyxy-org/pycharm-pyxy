package org.pyxy.pyxycharm.lang.psi.element

import com.jetbrains.python.psi.PyElementType

object PyxyElementTypes {
    val TAG = PyElementType("TAG") { PyxyTag(it) }
    val TAG_NAME = PyElementType("TAG_NAME") { PyxyTagObjectRef(it) }
    val ATTR_NAME = PyElementType("ATTR_NAME") { PyxyAttrName(it) }
    val ARG_LIST = PyElementType("ARG_LIST") { PyxyArgumentList(it) }
}
