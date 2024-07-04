package org.pyxy.pyxycharm.lang.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.TokenType
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.ExpressionParsing
import org.pyxy.pyxycharm.lang.psi.elementTypes.PyxyElementTypes

class PyxyExpressionParsing(context: PyxyParserContext): ExpressionParsing(context) {
    override fun getParsingContext() = myContext as PyxyParserContext

    override fun parsePrimaryExpression(isTargetExpression: Boolean): Boolean {
        myBuilder.setDebugMode(true)

        if (atToken(PyTokenTypes.LT)) {
            val tag = myBuilder.mark()
            nextToken()
            parseTagOpen(tag)
            return true
        }

        return super.parsePrimaryExpression(isTargetExpression)
    }

    fun parseTagOpen(tag: SyntaxTreeBuilder.Marker) {
        parseTagContents()

        var hasBody = true
        if (atToken(PyTokenTypes.DIV)) {
            hasBody = false
            nextToken()
        }

        checkMatches(PyTokenTypes.GT, "Expected tag close")

        if (!hasBody) {
            tag.done(PyxyElementTypes.TAG)
            return
        }

        var subtag: SyntaxTreeBuilder.Marker? = null
        while (!myBuilder.eof()) {
            // Get XML CDATA
            while (!myBuilder.eof() && !atToken(PyTokenTypes.LT)) {
                nextToken()
            }
            if (myBuilder.lookAhead(1) != PyTokenTypes.DIV) {
                subtag = myBuilder.mark()
            }
            nextToken()

            // If it's a tag close, then break
            if (atToken(PyTokenTypes.DIV)) break

            // Otherwise, parse the new tag being opened
            parseTagOpen(subtag!!)
            subtag = null
        }

        // '/' xml_tag_content '>'
        nextToken()
        parseTagContents()

        checkMatches(PyTokenTypes.GT, "Expected tag close")
        tag.done(PyxyElementTypes.TAG)
    }

    // xml_tag_content: ( xml_name xml_name* '=' (STRING | fstring_expr) |
    //                    xml_name xml_name* )+
    private fun parseTagContents() {
        if (!atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
            myBuilder.error("Expected tag name")
        }

        var atTagName = true
        val tagNameMarker = myBuilder.mark()

        while (!myBuilder.eof() && atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
            nextToken()
            if (atTagName && myBuilder.rawLookup(1) == TokenType.WHITE_SPACE) {
                atTagName = false
                tagNameMarker.done(PyxyElementTypes.TAG_NAME)
            }
            if (atToken(PyTokenTypes.EQ)) {
                nextToken()
                if (atAnyOfTokens(PyTokenTypes.STRING_NODES)) {
                    // name="..."
                    nextToken()
                    continue
                } else if (atToken(PyTokenTypes.LBRACE)) {
                    tagNameMarker.done(PyxyElementTypes.TAG_NAME)
                    myBuilder.error("TODO: curly braces not yet supported")
                    return
                }
                tagNameMarker.done(PyxyElementTypes.TAG_NAME)
                myBuilder.error("Expected string or curly braces")
                return
            }
        }

        if (atTagName) {
            tagNameMarker.done(PyxyElementTypes.TAG_NAME)
        }
    }

}