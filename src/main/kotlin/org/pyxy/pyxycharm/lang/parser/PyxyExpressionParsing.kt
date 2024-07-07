package org.pyxy.pyxycharm.lang.parser

import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyTokenTypes
import com.jetbrains.python.parsing.ExpressionParsing
import org.pyxy.pyxycharm.lang.psi.element.PyxyElementTypes

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
        parseTagContents(true)

        checkMatches(PyTokenTypes.GT, "Expected tag close")
        tag.done(PyxyElementTypes.TAG)
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
                attrName.done(PyxyElementTypes.KEYWORD_ARGUMENT_NAME)
            }

            if (atToken(PyTokenTypes.EQ)) {
                nextToken()
                if (atAnyOfTokens(PyTokenTypes.STRING_NODES)) {
                    // name="..."
                } else if (atToken(PyTokenTypes.LBRACE)) {
                    argListMarker.done(PyxyElementTypes.ARGUMENT_LIST)
                    myBuilder.error("TODO: curly braces not yet supported")
                    return
                } else {
                    argListMarker.done(PyxyElementTypes.ARGUMENT_LIST)
                    myBuilder.error("Expected string or curly braces")
                    return
                }
                nextToken()
            }
            attr.done(PyxyElementTypes.KEYWORD_ARGUMENT)
        }

        argListMarker.done(PyxyElementTypes.ARGUMENT_LIST)
    }
}