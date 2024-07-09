package org.pyxy.pyxycharm

import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PythonDialectsTokenSetContributor
import org.pyxy.pyxycharm.psi.element.PyxyElementTypes

class PyxyDialectTokenContributor : PythonDialectsTokenSetContributor {
    override fun getStatementTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getExpressionTokens(): TokenSet {
        return TokenSet.create(
            PyxyElementTypes.TAG_KEYWORD_ARGUMENT_EXPRESSION,
            PyxyElementTypes.TAG_NAME,
            PyxyElementTypes.PARENTHESIS_WRAPPER,
        )
    }

    override fun getKeywordTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getParameterTokens(): TokenSet {
        return TokenSet.EMPTY
//        return TokenSet.create(PyxyElementTypes.TAG_KEYWORD_ARGUMENT_EXPRESSION)
    }

    override fun getFunctionDeclarationTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getUnbalancedBracesRecoveryTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getReferenceExpressionTokens(): TokenSet {
        return TokenSet.create(PyxyElementTypes.TAG_NAME)
    }
}