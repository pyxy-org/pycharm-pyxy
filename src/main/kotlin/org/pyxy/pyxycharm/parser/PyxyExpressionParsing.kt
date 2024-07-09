package org.pyxy.pyxycharm.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.IElementType
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
        // Not sure why, but this fixes some `<... /> for ... in ...` expressions
        val parenExpression = tagExpression.precede()
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
            parenExpression.done(PyxyElementTypes.PARENTHESIS_WRAPPER)
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
                parseBraceContents()
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
        parenExpression.done(PyxyElementTypes.PARENTHESIS_WRAPPER)
    }

    private fun parseTagContents(isClosing: Boolean = false) {
        if (!parseTagName()) return
        if (isClosing) return

        val argListMarker = myBuilder.mark()

        while (!myBuilder.eof() && !atAnyOfTokens(TokenSet.create(PyTokenTypes.DIV, PyTokenTypes.GT))) {
            val attr = myBuilder.mark()
            val attrName = myBuilder.mark()
            var emptyName = true
            while (!myBuilder.eof() && atAnyOfTokens(PyxyTokenTypes.XML_NAME_TOKENS)) {
                emptyName = false
                if (PyTokenTypes.WHITESPACE_OR_LINEBREAK.contains(myBuilder.rawLookup(1))) {
                    nextToken()
                    break
                }
                nextToken()
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
                    parseBraceContents()
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
            parseTagNamePart(refExpr)
            refExpr.done(PyxyElementTypes.TAG_NAME)
            while (matchToken(PyTokenTypes.DOT)) {
                refExpr = refExpr.precede()
                parseTagNamePart(refExpr)
                refExpr.done(PyxyElementTypes.TAG_NAME)
            }
            return true
        }
        return false
    }

    private val TAG_NAME_JOINERS = TokenSet.create(PyTokenTypes.MINUS, PyTokenTypes.COLON)

    private fun parseTagNamePart(marker: SyntaxTreeBuilder.Marker): Boolean {
        // If there's only one token that makes up the name...
        if (whitespaceBeforeNextToken()) {
            return checkTagNameIdentifier(marker)
        }

        // In this path, there are multiple tokens that make up the name
        if (!checkTagNameIdentifier(marker)) return false

        while (TAG_NAME_JOINERS.contains(myBuilder.tokenType)) {
            if (whitespaceBeforeNextToken()) {
                // Can't end on a joiner
                marker.error("Incomplete tag name")
                return false
            }
            nextToken()

            val isLastIdentifier = whitespaceBeforeNextToken()
            if (!checkTagNameIdentifier(marker)) return false
            if (isLastIdentifier) break
        }
        return true
    }

    private fun checkTagNameIdentifier(marker: SyntaxTreeBuilder.Marker): Boolean {
        if (!atToken(PyTokenTypes.IDENTIFIER)) {
            marker.error("Bad tag name")
            return false
        }
        nextToken()
        return true
    }

    private fun whitespaceBeforeNextToken(): Boolean {
        return PyTokenTypes.WHITESPACE_OR_LINEBREAK.contains(myBuilder.rawLookup(1))
    }

    // Modified from ExpressionParsing.parseParenthesizedExpression
    private fun parseBraceContents() {
        val expr = myBuilder.mark()
        myBuilder.advanceLexer()
        if (myBuilder.tokenType === PyTokenTypes.RBRACE) {
            myBuilder.advanceLexer()
            expr.error("Empty brace expression")
        } else {
            parseYieldOrTupleExpression(false)
            if (atForOrAsyncFor()) {
                parseComprehension(expr, PyTokenTypes.RBRACE, PyElementTypes.GENERATOR_EXPRESSION)
            } else {
                val err = myBuilder.mark()
                var empty = true
                while (!myBuilder.eof() && myBuilder.tokenType !== PyTokenTypes.RBRACE && myBuilder.tokenType !== PyTokenTypes.LINE_BREAK && myBuilder.tokenType !== PyTokenTypes.STATEMENT_BREAK && myBuilder.tokenType !== PyTokenTypes.FSTRING_END) {
                    myBuilder.advanceLexer()
                    empty = false
                }
                if (!empty) {
                    err.error(PyParsingBundle.message("unexpected.expression.syntax"))
                } else {
                    err.drop()
                }
                checkMatches(PyTokenTypes.RBRACE, PyParsingBundle.message("PARSE.expected.rbrace"))
                expr.done(PyxyElementTypes.PARENTHESIS_WRAPPER)
            }
        }
    }

    // Copied from the private ExpressionParsing.atForOrAsyncFor
    private fun atForOrAsyncFor(): Boolean {
        if (atToken(PyTokenTypes.FOR_KEYWORD)) {
            return true
        } else if (matchToken(PyTokenTypes.ASYNC_KEYWORD)) {
            if (atToken(PyTokenTypes.FOR_KEYWORD)) {
                return true
            } else {
                myBuilder.error(PyParsingBundle.message("for.expected"))
                return false
            }
        }
        return false
    }

    // Copied from the private ExpressionParsing.parseComprehension
    private fun parseComprehension(
        expr: SyntaxTreeBuilder.Marker,
        endToken: IElementType?,
        exprType: IElementType
    ) {
        assertCurrentToken(PyTokenTypes.FOR_KEYWORD)
        while (true) {
            myBuilder.advanceLexer()
            parseStarTargets()
            parseComprehensionRange(exprType === PyElementTypes.GENERATOR_EXPRESSION)
            while (myBuilder.tokenType === PyTokenTypes.IF_KEYWORD) {
                myBuilder.advanceLexer()
                if (!parseOldExpression()) {
                    myBuilder.error(PyParsingBundle.message("PARSE.expected.expression"))
                }
            }
            if (atForOrAsyncFor()) {
                continue
            }
            if (endToken == null || matchToken(endToken)) {
                break
            }
            myBuilder.error(PyParsingBundle.message("PARSE.expected.for.or.bracket"))
            break
        }
        expr.done(exprType)
    }
}