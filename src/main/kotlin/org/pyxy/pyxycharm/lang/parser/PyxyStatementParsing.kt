package org.pyxy.pyxycharm.lang.parser

import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.StatementParsing

class PyxyStatementParsing(context: PyxyParserContext) : StatementParsing(context) {
    override fun getParsingContext() = myContext as PyxyParserContext

    override fun parseSimpleStatement(checkLanguageLevel: Boolean) {
        // xml: '<' xml_opened
        if (atToken(PyTokenTypes.LT)) {
            nextToken()
            parsingContext.expressionParser.parseTagOpen()
            return
        }

        super.parseSimpleStatement(checkLanguageLevel)
    }
}