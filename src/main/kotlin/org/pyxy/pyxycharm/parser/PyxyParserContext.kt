package org.pyxy.pyxycharm.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.lang.impl.PsiBuilderImpl
import com.jetbrains.python.parsing.ParsingContext
import com.jetbrains.python.psi.LanguageLevel

class PyxyParserContext(
    builder: SyntaxTreeBuilder,
    languageLevel: LanguageLevel
): ParsingContext(builder, languageLevel) {
    init {
        require(builder is PsiBuilderImpl) {
            "Parser must be PsiBuilderImpl in order for Pyxy parser to work properly"
        }
    }

    private val stmtParser = PyxyStatementParsing(this)
    private val exprParser = PyxyExpressionParsing(this)

    override fun getStatementParser() = stmtParser
    override fun getExpressionParser() = exprParser
}