package org.pyxy.pyxycharm.highlighter

import com.intellij.openapi.editor.XmlHighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Font


object PyxyHighlighterColors {
    val PYXY_TAG_NAME = TextAttributesKey.createTextAttributesKey(
        "PYXY_TAG_NAME", XmlHighlighterColors.HTML_TAG_NAME
    )
    val PYXY_TAG = TextAttributesKey.createTextAttributesKey(
        "PYXY_TAG", XmlHighlighterColors.HTML_TAG
    )
    val PYXY_TAG_CDATA = TextAttributesKey.createTextAttributesKey(
        "PYXY_TAG_CDATA", TextAttributes(
            EditorColorsManager.getInstance().globalScheme.defaultForeground, null, null, null, Font.PLAIN
        )
    )
    val PYXY_ATTRIBUTE_NAME = TextAttributesKey.createTextAttributesKey(
        "PYXY_ATTRIBUTE_NAME", XmlHighlighterColors.HTML_ATTRIBUTE_NAME
    )
    val PYXY_ATTRIBUTE_VALUE = TextAttributesKey.createTextAttributesKey(
        "PYXY_ATTRIBUTE_VALUE", XmlHighlighterColors.HTML_ATTRIBUTE_VALUE
    )
}
