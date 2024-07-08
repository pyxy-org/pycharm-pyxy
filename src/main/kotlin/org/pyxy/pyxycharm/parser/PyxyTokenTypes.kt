package org.pyxy.pyxycharm.parser

import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyTokenTypes

object PyxyTokenTypes {
    // xml_cdata_keywords: ( 'False' | 'None' | 'True' | 'and' | 'as' | 'assert' | 'async' | 'await' | 'break' | 'class' |
    //                      'continue' | 'def' | 'del' | 'elif' | 'else' | 'except' | 'finally' | 'for' | 'from' | 'global' |
    //                      'if' | 'import' | 'in' | 'is' | 'lambda' | 'nonlocal' | 'not' | 'or' | 'pass' | 'raise' |
    //                      'return' | 'try' | 'while' | 'with' | 'yield' )
    val KEYWORD_TOKENS = TokenSet.create(
        PyTokenTypes.FALSE_KEYWORD,
        PyTokenTypes.NONE_KEYWORD,
        PyTokenTypes.TRUE_KEYWORD,
        PyTokenTypes.AND_KEYWORD,
        PyTokenTypes.AS_KEYWORD,
        PyTokenTypes.ASSERT_KEYWORD,
        PyTokenTypes.ASYNC_KEYWORD,
        PyTokenTypes.AWAIT_KEYWORD,
        PyTokenTypes.BREAK_KEYWORD,
        PyTokenTypes.CLASS_KEYWORD,
        PyTokenTypes.CONTINUE_KEYWORD,
        PyTokenTypes.DEF_KEYWORD,
        PyTokenTypes.DEL_KEYWORD,
        PyTokenTypes.ELIF_KEYWORD,
        PyTokenTypes.ELSE_KEYWORD,
        PyTokenTypes.EXCEPT_KEYWORD,
        PyTokenTypes.FINALLY_KEYWORD,
        PyTokenTypes.FOR_KEYWORD,
        PyTokenTypes.FROM_KEYWORD,
        PyTokenTypes.GLOBAL_KEYWORD,
        PyTokenTypes.IF_KEYWORD,
        PyTokenTypes.IMPORT_KEYWORD,
        PyTokenTypes.IN_KEYWORD,
        PyTokenTypes.IS_KEYWORD,
        PyTokenTypes.LAMBDA_KEYWORD,
        PyTokenTypes.NONLOCAL_KEYWORD,
        PyTokenTypes.NOT_KEYWORD,
        PyTokenTypes.OR_KEYWORD,
        PyTokenTypes.PASS_KEYWORD,
        PyTokenTypes.RAISE_KEYWORD,
        PyTokenTypes.RETURN_KEYWORD,
        PyTokenTypes.TRY_KEYWORD,
        PyTokenTypes.WHILE_KEYWORD,
        PyTokenTypes.WITH_KEYWORD,
        PyTokenTypes.YIELD_KEYWORD,
    )

    // xml_name: (NAME | NUMBER | xml_cdata_keywords | ':' | '-' | '.')
    val XML_NAME_TOKENS = TokenSet.orSet(
        TokenSet.create(PyTokenTypes.IDENTIFIER),
        PyTokenTypes.NUMERIC_LITERALS,
        KEYWORD_TOKENS,
        TokenSet.create(
            PyTokenTypes.COLON, PyTokenTypes.MINUS, PyTokenTypes.DOT
        ),
    )
}