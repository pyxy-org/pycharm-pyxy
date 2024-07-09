package org.pyxy.pyxycharm.highlighter

import com.intellij.lang.annotation.HighlightSeverity
import com.jetbrains.python.validation.PyAnnotator
import org.pyxy.pyxycharm.psi.element.*

class PyxyAnnotatingVisitor : PyAnnotator() {
    fun visitPyxyTag(node: PyxyTag) {
        super.visitPyElement(node)
        for (tagNode in node.getHighlightedTagNodes()) {
            addHighlightingAnnotation(tagNode, PyxyHighlighterColors.PYXY_TAG, HighlightSeverity.TEXT_ATTRIBUTES)
        }
    }

    fun visitPyxyTagName(node: PyxyTagName) {
        super.visitPyReferenceExpression(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_TAG_NAME, HighlightSeverity.TEXT_ATTRIBUTES)
    }

    fun visitPyxyAttribute(node: PyxyKeywordArgument) {
        super.visitPyKeywordArgument(node)
        for (valueNode in node.getHighlightedValueNodes()) {
            addHighlightingAnnotation(valueNode, PyxyHighlighterColors.PYXY_ATTRIBUTE_VALUE, HighlightSeverity.TEXT_ATTRIBUTES)
        }
        for (braceNode in node.getHighlightedBraceNodes()) {
            addHighlightingAnnotation(braceNode, PyxyHighlighterColors.PYXY_TAG_CDATA, HighlightSeverity.TEXT_ATTRIBUTES)
        }
    }

    fun visitPyxyAttributeName(node: PyxyKeywordArgumentName) {
        super.visitPyStringLiteralExpression(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_ATTRIBUTE_NAME, HighlightSeverity.TEXT_ATTRIBUTES)
    }

    fun visitPyxyCData(node: PyxyTagCData) {
        super.visitPyElement(node)
        addHighlightingAnnotation(node, PyxyHighlighterColors.PYXY_TAG_CDATA, HighlightSeverity.TEXT_ATTRIBUTES)
    }
}