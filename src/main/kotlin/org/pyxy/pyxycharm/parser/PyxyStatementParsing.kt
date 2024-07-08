package org.pyxy.pyxycharm.parser

import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.StatementParsing

class PyxyStatementParsing(context: PyxyParserContext) : StatementParsing(context) {
    override fun getParsingContext() = myContext as PyxyParserContext

    override fun parseSimpleStatement(checkLanguageLevel: Boolean) {
        // xml: '<' xml_opened
        if (atToken(PyTokenTypes.LT)) {
            val tagExpression = myBuilder.mark()
            val tag = myBuilder.mark()
            nextToken()
            parsingContext.expressionParser.parseTagOpen(tag, tagExpression)
            return
        }

        super.parseSimpleStatement(checkLanguageLevel)
    }
}