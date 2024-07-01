package org.pyxy.pyxycharm.lang.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.jetbrains.python.parsing.ParsingContext
import com.jetbrains.python.parsing.PyParser
import com.jetbrains.python.psi.LanguageLevel

class PyxyParser : PyParser() {
    override fun createParsingContext(
        builder: SyntaxTreeBuilder?,
        languageLevel: LanguageLevel?
    ): ParsingContext = PyxyParserContext(builder!!, languageLevel!!)
}