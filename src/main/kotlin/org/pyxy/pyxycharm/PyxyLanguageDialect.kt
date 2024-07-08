package org.pyxy.pyxycharm

import com.intellij.lang.Language
import com.jetbrains.python.PythonLanguage

object PyxyLanguageDialect : Language(PythonLanguage.getInstance(), "Python (pyxy)") {
     val fileElementType = PyxyFileElementType(this)

     private fun readResolve(): Any = PyxyLanguageDialect
}