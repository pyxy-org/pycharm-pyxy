package org.pyxy.pyxycharm.lang.highlighter

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey


object PyxyHighlighterColors {
    val PYXY_EMBEDDED = TextAttributesKey.createTextAttributesKey(
        "PYXY_EMBEDDED", DefaultLanguageHighlighterColors.TEMPLATE_LANGUAGE_COLOR)
    val PYXY_TAG_NAME = TextAttributesKey.createTextAttributesKey(
        "PYXY_TAG_NAME", XmlHighlighterColors.HTML_TAG_NAME)
    val PYXY_TAG = TextAttributesKey.createTextAttributesKey(
        "PYXY_TAG", XmlHighlighterColors.HTML_TAG)
    val PYXY_ATTRIBUTE_NAME = TextAttributesKey.createTextAttributesKey(
        "PYXY_ATTRIBUTE_NAME", XmlHighlighterColors.HTML_ATTRIBUTE_NAME)
    val PYXY_ATTRIBUTE_VALUE = TextAttributesKey.createTextAttributesKey(
        "PYXY_ATTRIBUTE_VALUE", XmlHighlighterColors.HTML_ATTRIBUTE_VALUE)
}
