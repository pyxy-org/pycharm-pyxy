package org.pyxy.pyxycharm.lang.highlighter

import com.jetbrains.python.psi.PyElementType


object PyxlTokenTypes {
    @JvmField val BADCHAR = PyElementType("PYXL BAD CHAR")
    @JvmField val ATTRNAME = PyElementType("PYXL ATTRNAME")
    @JvmField val ATTRVALUE = PyElementType("PYXL ATTRVALUE")
    @JvmField val ATTRVALUE_START = PyElementType("PYXL ATTRVALUE BEGIN")
    @JvmField val ATTRVALUE_END = PyElementType("PYXL ATTRVALUE END")

    @JvmField val TAGBEGIN = PyElementType("PYXL TAGBEGIN <")
    @JvmField val TAGNAME_MODULE = PyElementType("PYXL TAGNAME_MODULE")
    @JvmField val TAGNAME = PyElementType("PYXL TAGNAME")
    @JvmField val TAGEND = PyElementType("PYXL TAGEND >")
    @JvmField val TAGCLOSE = PyElementType("PYXL TAGCLOSE </")

    @JvmField val TAGENDANDCLOSE = PyElementType("PYXL TAGENDANDCLOSE />")

    @JvmField val BUILT_IN_TAG = PyElementType("PYXL BUILT IN TAG")

    @JvmField val EMBED_START = PyElementType("PYXL PYTHON EMBED BEGIN {")
    @JvmField val EMBED_END = PyElementType("PYXL PYTHON EMBED END }")
    @JvmField val STRING = PyElementType("PYXL STRING")
}