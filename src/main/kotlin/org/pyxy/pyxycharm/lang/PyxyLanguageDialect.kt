package org.pyxy.pyxycharm.lang

import com.intellij.lang.Language
import com.jetbrains.python.PythonLanguage

object PyxyLanguageDialect : Language(PythonLanguage.getInstance(), "Pyxy") {
     val fileElementType = PyxyFileElementType(this)
}