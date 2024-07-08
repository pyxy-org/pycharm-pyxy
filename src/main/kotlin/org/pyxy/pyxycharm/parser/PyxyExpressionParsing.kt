package org.pyxy.pyxycharm.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyElementTypes
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.ExpressionParsing
import org.pyxy.pyxycharm.lang.psi.element.PyxyElementTypes

class PyxyExpressionParsing(context: PyxyParserContext): ExpressionParsing(context) {
    override fun getParsingContext() = myContext as PyxyParserContext

    override fun parsePrimaryExpression(isTargetExpression: Boolean): Boolean {
        myBuilder.setDebugMode(true)

        if (atToken(PyTokenTypes.LT)) {
            val tagExpression = myBuilder.mark()
            val tag = myBuilder.mark()
            nextToken()
            parseTagOpen(tag, tagExpression)
            return true
        }

        return super.parsePrimaryExpression(isTargetExpression)
    }

    fun parseTagOpen(tag: SyntaxTreeBuilder.Marker, tagExpression: SyntaxTreeBuilder.Marker) {
        parseTagContents()

        var hasBody = true
        if (atToken(PyTokenTypes.DIV)) {
            hasBody = false
            nextToken()
        }

        checkMatches(PyTokenTypes.GT, "Expected tag close")
        tag.done(PyxyElementTypes.TAG)

        if (!hasBody) {
            tagExpression.done(PyxyElementTypes.TAG_EXPRESSION)
            return
        }

        var subtagExpression: SyntaxTreeBuilder.Marker? = null
        var subOrClosingTag: SyntaxTreeBuilder.Marker? = null
        while (!myBuilder.eof()) {
            // Get XML CDATA
            if (!atToken(PyTokenTypes.LT)) {
                val cdata: SyntaxTreeBuilder.Marker = myBuilder.mark()
                while (!myBuilder.eof() && !atToken(PyTokenTypes.LT)) {
                    nextToken()
                }
                cdata.done(PyxyElementTypes.TAG_CDATA)
            }

            if (myBuilder.lookAhead(1) != PyTokenTypes.DIV) {
                subtagExpression = myBuilder.mark()
            }
            subOrClosingTag = myBuilder.mark()
            nextToken()

            // If it's a tag close, then break
            if (atToken(PyTokenTypes.DIV)) break

            // Otherwise, parse the new tag being opened
            parseTagOpen(subOrClosingTag, subtagExpression!!)
            subtagExpression = null
            subOrClosingTag = null
        }

        // '/' xml_tag_content '>'
        nextToken()
        parseTagContents(true)

        checkMatches(PyTokenTypes.GT, "Expected tag close")
        subOrClosingTag?.done(PyxyElementTypes.TAG)
        tagExpression.done(PyxyElementTypes.TAG_EXPRESSION)
    }

    private fun parseTagContents(isClosing: Boolean = false) {
        if (!atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
            myBuilder.error("Expected tag name")
        }

        val tagNameMarker = myBuilder.mark()
        while (!myBuilder.eof() && atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
            if (PyTokenTypes.WHITESPACE_OR_LINEBREAK.contains(myBuilder.rawLookup(1))) {
                nextToken()
                break
            }
            nextToken()
        }
        tagNameMarker.done(PyxyElementTypes.TAG_NAME)

        if (isClosing) return

        val argListMarker = myBuilder.mark()

        while (!myBuilder.eof() && !atAnyOfTokens(TokenSet.create(PyTokenTypes.DIV, PyTokenTypes.GT))) {
            val attr = myBuilder.mark()
            val attrName = myBuilder.mark()
            var emptyName = true
            while (!myBuilder.eof() && atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
                if (PyTokenTypes.WHITESPACE_OR_LINEBREAK.contains(myBuilder.rawLookup(1))) {
                    if (!emptyName) attrName.done(PyxyElementTypes.TAG_NAME)
                    break
                }
                nextToken()
                emptyName = false
            }

            if (emptyName) {
                attrName.drop()
                attr.drop()
                argListMarker.drop()
                myBuilder.error("Attribute name expected")
                while (!myBuilder.eof() && !atAnyOfTokens(TokenSet.create(PyTokenTypes.DIV, PyTokenTypes.GT))) {
                    nextToken()
                }
                return
            } else {
                attrName.done(PyxyElementTypes.TAG_KEYWORD_ARGUMENT_NAME)
            }

            if (atToken(PyTokenTypes.EQ)) {
                nextToken()
                if (!parseStringLiteralExpression()) {
                    myBuilder.error("Expected string literal")
                }
            }
            attr.done(PyxyElementTypes.TAG_KEYWORD_ARGUMENT_EXPRESSION)
        }

        argListMarker.done(PyElementTypes.ARGUMENT_LIST)
    }
}