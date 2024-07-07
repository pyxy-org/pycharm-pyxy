package org.pyxy.pyxycharm.lang

import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PythonDialectsTokenSetContributor
import org.pyxy.pyxycharm.lang.psi.element.PyxyElementTypes


class PyxyDialectTokenContributor : PythonDialectsTokenSetContributor {
    override fun getStatementTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getExpressionTokens(): TokenSet {
        return TokenSet.create(PyxyElementTypes.TAG_KEYWORD_ARGUMENT_EXPRESSION)
    }

    override fun getKeywordTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getParameterTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getFunctionDeclarationTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getUnbalancedBracesRecoveryTokens(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun getReferenceExpressionTokens(): TokenSet {
        return TokenSet.EMPTY
    }
}