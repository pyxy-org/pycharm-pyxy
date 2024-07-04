package org.pyxy.pyxycharm.lang.psi.elementTypes

import com.jetbrains.python.psi.PyElementType
import org.pyxy.pyxycharm.lang.psi.PyxyMarkupExpression

object PyxyElementTypes {
    val TAG = PyElementType("TAG") {
        PyxyMarkupExpression(it)
    }

    val TAG_NAME = PyElementType("TAG_NAME") {
        PyxyMarkupExpression(it)
    }
}