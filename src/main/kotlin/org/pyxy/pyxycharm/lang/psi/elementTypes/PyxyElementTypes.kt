package org.pyxy.pyxycharm.lang.psi.elementTypes

import com.jetbrains.python.psi.PyElementType
import org.pyxy.pyxycharm.lang.psi.PyxyMarkupExpression

object PyxyElementTypes {
    val MARKUP_EXPRESSION = PyElementType("PYXY_MARKUP_EXPRESSION") {
        PyxyMarkupExpression(it)
    }
}