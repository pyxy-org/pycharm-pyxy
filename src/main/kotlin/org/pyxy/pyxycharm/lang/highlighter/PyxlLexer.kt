package org.pyxy.pyxycharm.lang.highlighter

import com.intellij.lexer.FlexAdapter
import java.io.Reader


open class PyxlLexer : FlexAdapter(_PyxlLexer(null as Reader?))