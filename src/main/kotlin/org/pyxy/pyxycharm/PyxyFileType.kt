package org.pyxy.pyxycharm

import com.jetbrains.python.PythonFileType

class PyxyFileType : PythonFileType(PyxyLanguageDialect) {

    override fun getName() = "Python (pyxy)"
    override fun getDefaultExtension() = "pyxy"
    override fun getDescription() = "Python (pyxy)"

}

val INSTANCE = PyxyFileType()