package org.pyxy.pyxycharm.lang.highlighter

import com.intellij.psi.tree.IElementType
import com.jetbrains.python.lexer.PythonLexer
import com.jetbrains.python.psi.LanguageLevel

class PyxyHighlighterLexer(languageLevel: LanguageLevel) : PythonLexer() {
    private val mLanguageLevel = languageLevel

    override fun getTokenType(): IElementType? {
        return super.getTokenType()
    }
}
