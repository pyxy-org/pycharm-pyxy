package org.pyxy.pyxycharm.parser

import com.intellij.psi.tree.TokenSet
import com.jetbrains.python.PyTokenTypes

object PyxyTokenTypes {
    // TODO: Does this cover everything?
    val STRING_TOKENS = TokenSet.orSet(
        PyTokenTypes.STRING_NODES,
        PyTokenTypes.FSTRING_TOKENS,
        PyTokenTypes.FSTRING_TEXT_TOKENS,
    )

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

    // xml_cdata_special: NAME | NUMBER | strings | NEWLINE | INDENT | DEDENT
    val XML_CDATA_SPECIAL_TOKENS = TokenSet.orSet(
        TokenSet.create(PyTokenTypes.IDENTIFIER),
        PyTokenTypes.NUMERIC_LITERALS,
        STRING_TOKENS,
        TokenSet.create(
            PyTokenTypes.LINE_BREAK,
            PyTokenTypes.INDENT,
            PyTokenTypes.DEDENT),
    )

    // xml_cdata_trigraph: '//=' | '**=' | '...'
    val XML_CDATA_TRIGRAPH_TOKENS = TokenSet.create(
        PyTokenTypes.FLOORDIVEQ,
        PyTokenTypes.EXPEQ,
        // There is no ellipsis token, I think it's just three dot tokens
    )

    // xml_cdata_digraph: '->' | '**' | '==' | '!=' | ':=' | '//' | '+=' | '-=' | '*=' | '@=' | '/=' | '%=' | '&=' | '|=' | '^='
    val XML_CDATA_DIGRAPH_TOKENS = TokenSet.create(
        PyTokenTypes.RARROW,
        PyTokenTypes.EXP,
        PyTokenTypes.EQEQ,
        PyTokenTypes.NE,
        PyTokenTypes.COLONEQ,
        PyTokenTypes.FLOORDIV,
        PyTokenTypes.PLUSEQ,
        PyTokenTypes.MINUSEQ,
        PyTokenTypes.MULTEQ,
        PyTokenTypes.ATEQ,
        PyTokenTypes.DIVEQ,
        PyTokenTypes.PERCEQ,
        PyTokenTypes.ANDEQ,
        PyTokenTypes.OREQ,
        PyTokenTypes.XOREQ
    )

    // xml_cdata_ascii: '!' | '#' | '$' | '%' | '&' | '(' | '*' | '+' | ',' | '-' | '.' | '/' | ':' | ';' | '=' | '?' | '@' | '[' | '^' | '`' | '|' | '~'
    val XML_CDATA_ASCII_TOKENS = TokenSet.create(
        PyTokenTypes.BAD_CHARACTER,  // Should handle missing chars
        // No exclamation point
        PyTokenTypes.END_OF_LINE_COMMENT,
        // No dollar
        PyTokenTypes.PERC,
        PyTokenTypes.AND,
        PyTokenTypes.LPAR,
        PyTokenTypes.MULT,
        PyTokenTypes.PLUS,
        PyTokenTypes.COMMA,
        PyTokenTypes.MINUS,
        PyTokenTypes.DOT,
        PyTokenTypes.DIV,
        PyTokenTypes.COLON,
        PyTokenTypes.SEMICOLON,
        PyTokenTypes.EQ,
        PyTokenTypes.AT,
        PyTokenTypes.LBRACKET,
        PyTokenTypes.XOR,
        PyTokenTypes.TICK,
        PyTokenTypes.OR,
        PyTokenTypes.TILDE,
    )

    // xml_cdata_start: xml_cdata_special | xml_cdata_keywords | xml_cdata_trigraph | xml_cdata_digraph | xml_cdata_ascii
    val XML_CDATA_START_TOKENS = TokenSet.orSet(
        XML_CDATA_SPECIAL_TOKENS,
        KEYWORD_TOKENS,
        XML_CDATA_TRIGRAPH_TOKENS,
        XML_CDATA_DIGRAPH_TOKENS,
        XML_CDATA_ASCII_TOKENS,
    )

    // xml_cdata_non_start: ')' | ']'
    val XML_CDATA_NON_START_TOKENS = TokenSet.create(
        PyTokenTypes.RPAR,
        PyTokenTypes.RBRACKET,
    )

    // xml_name: (NAME | NUMBER | xml_cdata_keywords | ':' | '-' | '.')
    val XML_NAME_TOKENS = TokenSet.orSet(
        TokenSet.create(PyTokenTypes.IDENTIFIER),
        PyTokenTypes.NUMERIC_LITERALS,
        KEYWORD_TOKENS,
        TokenSet.create(
            PyTokenTypes.COLON,
            PyTokenTypes.MINUS,
            PyTokenTypes.DOT),
    )
}