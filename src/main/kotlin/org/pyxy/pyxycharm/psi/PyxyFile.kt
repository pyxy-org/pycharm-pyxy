package org.pyxy.pyxycharm.psi

import com.intellij.psi.FileViewProvider
import com.jetbrains.python.psi.impl.PyFileImpl
import org.pyxy.pyxycharm.INSTANCE
import org.pyxy.pyxycharm.PyxyLanguageDialect
import javax.swing.Icon

class PyxyFile(viewProvider: FileViewProvider) : PyFileImpl(viewProvider, PyxyLanguageDialect) {
    override fun getIcon(flags: Int): Icon = INSTANCE.icon
    override fun toString() = "PyxyFile: $name"
    override fun getStub() = null
}
