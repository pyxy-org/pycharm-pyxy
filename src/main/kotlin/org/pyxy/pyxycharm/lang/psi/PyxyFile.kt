package org.pyxy.pyxycharm.lang.psi

import com.intellij.psi.FileViewProvider
import com.jetbrains.python.psi.impl.PyFileImpl
import org.pyxy.pyxycharm.PyxyFileType
import org.pyxy.pyxycharm.lang.PyxyLanguageDialect
import javax.swing.Icon

class PyxyFile(viewProvider: FileViewProvider): PyFileImpl(viewProvider, PyxyLanguageDialect) {
    override fun getIcon(flags: Int): Icon = PyxyFileType.icon
    override fun toString() = "PyxyFile: $name"
    override fun getStub() = null
}
