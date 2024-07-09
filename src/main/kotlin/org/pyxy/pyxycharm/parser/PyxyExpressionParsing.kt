package org.pyxy.pyxycharm.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyElementTypes
import com.jetbrains.python.PyParsingBundle
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.ExpressionParsing
import org.pyxy.pyxycharm.psi.element.PyxyElementTypes

class PyxyExpressionParsing(context: PyxyParserContext) : ExpressionParsing(context) {
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
        parseTagContents(false)

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
            if (!atToken(PyTokenTypes.LT) && !atToken(PyTokenTypes.LBRACE)) {
                val cdata: SyntaxTreeBuilder.Marker = myBuilder.mark()
                while (!myBuilder.eof() && !atToken(PyTokenTypes.LT) && !atToken(PyTokenTypes.LBRACE)) {
                    nextToken()
                }
                cdata.done(PyxyElementTypes.TAG_CDATA)
            }

            if (atToken(PyTokenTypes.LBRACE)) {
                nextToken()
                parseExpression()
                checkMatches(PyTokenTypes.RBRACE, "Expected closing brace")
                continue
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

        parseTagName()

        if (isClosing) return

        val argListMarker = myBuilder.mark()

        while (!myBuilder.eof() && !atAnyOfTokens(TokenSet.create(PyTokenTypes.DIV, PyTokenTypes.GT))) {
            val attr = myBuilder.mark()
            val attrName = myBuilder.mark()
            var emptyName = true
            while (!myBuilder.eof() && atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
                if (PyTokenTypes.WHITESPACE_OR_LINEBREAK.contains(myBuilder.rawLookup(1))) {
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
                if (parseStringLiteralExpression()) {
                    // Success
                } else if (atToken(PyTokenTypes.LBRACE)) {
                    nextToken()
                    parseExpression()
                    checkMatches(PyTokenTypes.RBRACE, "Expected closing brace")
                } else {
                    myBuilder.error("Expected string literal or {expression}")
                }
            }
            attr.done(PyxyElementTypes.TAG_KEYWORD_ARGUMENT_EXPRESSION)
        }

        argListMarker.done(PyElementTypes.ARGUMENT_LIST)
    }

    private fun parseTagName(): Boolean {
        if (atToken(PyTokenTypes.IDENTIFIER)) {
            var refExpr = myBuilder.mark()
            nextToken()
            refExpr.done(PyxyElementTypes.TAG_NAME)
            while (matchToken(PyTokenTypes.DOT)) {
                refExpr = refExpr.precede()
                checkMatches(PyTokenTypes.IDENTIFIER, PyParsingBundle.message("PARSE.expected.name"))
                refExpr.done(PyxyElementTypes.TAG_NAME)
            }
            return true
        }
        return false
    }
}