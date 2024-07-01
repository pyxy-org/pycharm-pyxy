package org.pyxy.pyxycharm.lang.parser

import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.ExpressionParsing
import org.pyxy.pyxycharm.lang.psi.elementTypes.PyxyElementTypes

class PyxyExpressionParsing(context: PyxyParserContext): ExpressionParsing(context) {
    override fun getParsingContext() = myContext as PyxyParserContext

    override fun parsePrimaryExpression(isTargetExpression: Boolean): Boolean {
        if (atToken(PyTokenTypes.LT)) {
            nextToken()
            parseTagOpen()
            return true
        }

        return super.parsePrimaryExpression(isTargetExpression)
    }

    // xml_opened: xml_tag_content ('/' '>' |
    //                              '>' (fstring_expr | xml_cdata)* '<' ('/' xml_tag_content '>' |
    //                                                                   xml_opened ('<' xml_opened)* '<' '/' xml_tag_content '>'))
    fun parseTagOpen() {
        val marker = myBuilder.mark()
        parseTagContents()

        var hasBody = true
        if (atToken(PyTokenTypes.DIV)) {
            hasBody = false
            nextToken()
        }

        checkMatches(PyTokenTypes.GT, "Expected tag close")

        if (!hasBody) {
            marker.done(PyxyElementTypes.MARKUP_EXPRESSION)
            return
        }

        while (!myBuilder.eof()) {
            // Get XML CDATA
            while (!myBuilder.eof() && !atToken(PyTokenTypes.LT)) {
                nextToken()
            }
            nextToken()

            // If it's a tag close, then break
            if (atToken(PyTokenTypes.DIV)) break

            // Otherwise, parse the new tag being opened
            parseTagOpen()
        }

        // '/' xml_tag_content '>'
        nextToken()
        parseTagContents()

        checkMatches(PyTokenTypes.GT, "Expected tag close")
        marker.done(PyxyElementTypes.MARKUP_EXPRESSION)
    }

    // xml_tag_content: ( xml_name xml_name* '=' (STRING | fstring_expr) |
    //                    xml_name xml_name* )+
    private fun parseTagContents() {
        if (!atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
            myBuilder.error("Expected tag name")
        }

        while (!myBuilder.eof() && atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
            nextToken()
            if (atToken(PyTokenTypes.EQ)) {
                nextToken()
                if (atAnyOfTokens(PyTokenTypes.STRING_NODES)) {
                    // name="..."
                    nextToken()
                    continue
                } else if (atToken(PyTokenTypes.LBRACE)) {
                    myBuilder.error("TODO: curly braces not yet supported")
                    return
                }
                myBuilder.error("Expected string or curly braces")
                return
            }
        }
    }

}