package org.pyxy.pyxycharm

import com.jetbrains.python.PythonFileType
import org.pyxy.pyxycharm.lang.PyxyLanguageDialect

object PyxyFileType: PythonFileType(PyxyLanguageDialect) {
    override fun getName() = "Python (pyxy)"
    override fun getDefaultExtension() = "pyxy"
    override fun getDescription() = "Python (pyxy)"
}