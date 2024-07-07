package org.pyxy.pyxycharm.lang.highlighter

import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.python.highlighting.PySyntaxHighlighterFactoryBase
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.psi.PyFile

class PyxyHighlighterFactory : PySyntaxHighlighterFactoryBase() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return PyxyHighlighter(getLanguageLevelForFile(virtualFile))
    }

    private fun getLanguageLevelForFile(virtualFile: VirtualFile?): LanguageLevel {
        if (virtualFile is PyFile) {
            return (virtualFile as PyFile).languageLevel
        }
        return LanguageLevel.getDefault()
    }
}