package org.pyxy.pyxycharm.lang.highlighter

import com.intellij.lang.annotation.HighlightSeverity
import com.jetbrains.python.validation.PyAnnotator
import org.pyxy.pyxycharm.lang.psi.element.*

class PyxyAnnotatingVisitor : PyAnnotator() {
    fun visitPyxyTag(node: PyxyTag) {
        super.visitPyElement(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_TAG, HighlightSeverity.TEXT_ATTRIBUTES)
    }

    fun visitPyxyTagName(node: PyxyTagName) {
        super.visitPyReferenceExpression(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_TAG_NAME, HighlightSeverity.TEXT_ATTRIBUTES)
    }

    fun visitPyxyAttribute(node: PyxyKeywordArgument) {
        super.visitPyKeywordArgument(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_ATTRIBUTE_VALUE, HighlightSeverity.TEXT_ATTRIBUTES)
    }

    fun visitPyxyAttributeName(node: PyxyKeywordArgumentName) {
        super.visitPyStringLiteralExpression(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_ATTRIBUTE_NAME, HighlightSeverity.TEXT_ATTRIBUTES)
    }

    fun visitPyxyCData(node: PyxyTagCData) {
        super.visitPyElement(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_ATTRIBUTE_VALUE, HighlightSeverity.TEXT_ATTRIBUTES)
    }
}