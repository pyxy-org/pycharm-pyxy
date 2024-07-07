package org.pyxy.pyxycharm.lang.highlighter

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.tree.IElementType
import com.jetbrains.python.highlighting.PyHighlighter
import com.jetbrains.python.psi.LanguageLevel
//import org.pyxy.pyxycharm.lang.psi.element.PyxyElementTypes

internal class PyxyHighlighter(languageLevel: LanguageLevel) : PyHighlighter(languageLevel) {
    private val myLanguageLevel: LanguageLevel = languageLevel

    override fun getHighlightingLexer(): Lexer {
        return PyxyHighlighterLexer(myLanguageLevel)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val pythonTextAttributesKeys: Array<TextAttributesKey> = super.getTokenHighlights(tokenType)
        return pack(pythonTextAttributesKeys, colorForTokenType[tokenType])
    }

    companion object {
        private val colorForTokenType: MutableMap<IElementType, TextAttributesKey> = HashMap()

        init {
//            colorForTokenType[PyxyElementTypes.TAG_EXPRESSION] = PyxyHighlighterColors.PYXY_TAG
//            colorForTokenType[PyxlTokenTypes.TAGCLOSE] = PyxyHighlighterColors.PYXY_TAG
//            colorForTokenType[PyxlTokenTypes.TAGEND] = PyxyHighlighterColors.PYXY_TAG
//            colorForTokenType[PyxlTokenTypes.TAGENDANDCLOSE] = PyxyHighlighterColors.PYXY_TAG
//
//            colorForTokenType[PyxlElementTypes.TAG] = PyxyHighlighterColors.PYXY_EMBEDDED
//
//            colorForTokenType[PyxlTokenTypes.TAGNAME] = PyxyHighlighterColors.PYXY_TAG_NAME
//            colorForTokenType[PyxlTokenTypes.TAGNAME_MODULE] = PyxyHighlighterColors.PYXY_TAG_NAME
//            colorForTokenType[PyxlTokenTypes.BUILT_IN_TAG] = PyxyHighlighterColors.PYXY_TAG_NAME
//
//            colorForTokenType[PyxlTokenTypes.ATTRNAME] = PyxyHighlighterColors.PYXY_ATTRIBUTE_NAME
//            colorForTokenType[PyxlTokenTypes.ATTRVALUE] = PyxyHighlighterColors.PYXY_ATTRIBUTE_VALUE
        }
    }
}
