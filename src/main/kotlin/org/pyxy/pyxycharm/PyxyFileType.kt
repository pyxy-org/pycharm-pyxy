package org.pyxy.pyxycharm

import com.jetbrains.python.PythonFileType
import org.pyxy.pyxycharm.lang.PyxyLanguageDialect

class PyxyFileType private constructor() : PythonFileType(PyxyLanguageDialect) {
    companion object {
        val INSTANCE = PyxyFileType()
    }

    override fun getName() = "Python (pyxy)"
    override fun getDefaultExtension() = "pyxy"
    override fun getDescription() = "Python (pyxy)"

}