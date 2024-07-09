package org.pyxy.pyxycharm.psi.element

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.PythonRuntimeService
import com.jetbrains.python.psi.PyElementVisitor
import com.jetbrains.python.psi.PyFromImportStatement
import com.jetbrains.python.psi.PyImportElement
import com.jetbrains.python.psi.impl.PyReferenceExpressionImpl
import com.jetbrains.python.psi.impl.references.PyImportReference
import com.jetbrains.python.psi.impl.references.PyQualifiedReference
import com.jetbrains.python.psi.impl.references.PyReferenceImpl
import com.jetbrains.python.psi.resolve.PyResolveContext
import org.pyxy.pyxycharm.highlighter.PyxyAnnotatingVisitor

class PyxyTagName(node: ASTNode) : PyReferenceExpressionImpl(node) {
    override fun getReference(context: PyResolveContext): PsiPolyVariantReference {
        val superRef = super.getReference(context)
        if (superRef.resolve() == null) {
            // TODO: If there are no . in the expression, resolve as a member of htpy
        }
        return superRef
    }

    override fun acceptPyVisitor(pyVisitor: PyElementVisitor) = when (pyVisitor) {
        is PyxyAnnotatingVisitor -> pyVisitor.visitPyxyTagName(this)
        else -> super.acceptPyVisitor(pyVisitor)
    }

    override fun toString(): String {
        return "${this::class.simpleName}: $text"
    }
}