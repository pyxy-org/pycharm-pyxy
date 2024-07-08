package org.pyxy.pyxycharm.parser

import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.jetbrains.python.PythonParserDefinition
import org.pyxy.pyxycharm.PyxyLanguageDialect
import org.pyxy.pyxycharm.psi.PyxyFile

class PyxyParserDefinition: PythonParserDefinition() {
    override fun createParser(project: Project): PsiParser = PyxyParser()

    override fun getFileNodeType() = PyxyLanguageDialect.fileElementType

    override fun createFile(viewProvider: FileViewProvider) = PyxyFile(viewProvider)
}