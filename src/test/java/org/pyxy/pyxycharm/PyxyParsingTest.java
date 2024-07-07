package org.pyxy.pyxycharm;

import com.intellij.lang.LanguageASTFactory;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.testFramework.ParsingTestCase;
import com.jetbrains.python.*;
import com.jetbrains.python.psi.PyPsiFacade;
import com.jetbrains.python.psi.impl.PyPsiFacadeImpl;
import com.jetbrains.python.psi.impl.PythonASTFactory;
import org.pyxy.pyxycharm.lang.parser.PyxyParserDefinition;

public class PyxyParsingTest extends ParsingTestCase {
    public PyxyParsingTest() {
        super("", "pyxy", new PyxyParserDefinition(), new PythonParserDefinition());
    }

    @Override
    protected void setUp() throws Exception {
        // https://github.com/JetBrains/intellij-community/blob/4984da1c3da85d7ed59314865f4d572d71225388/python/testSrc/com/jetbrains/python/parsing/PythonParsingTest.java#L36
        super.setUp();
        Registry.markAsLoaded();
        registerExtensionPoint(PythonDialectsTokenSetContributor.EP_NAME, PythonDialectsTokenSetContributor.class);
        registerExtension(PythonDialectsTokenSetContributor.EP_NAME, new PythonTokenSetContributor());
        addExplicitExtension(LanguageASTFactory.INSTANCE, PythonLanguage.getInstance(), new PythonASTFactory());
        getProject().registerService(PyPsiFacade.class, PyPsiFacadeImpl.class);
        getApplication().registerService(PyElementTypesFacade.class, PyElementTypesFacadeImpl.class);
        getApplication().registerService(PyLanguageFacade.class, PyLanguageFacadeImpl.class);
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/testdata";
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testBasic() {
        doTest(true);
    }
}
