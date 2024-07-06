import com.intellij.testFramework.ParsingTestCase;
import org.pyxy.pyxycharm.lang.parser.PyxyParserDefinition;

public class PyxyParsingTest extends ParsingTestCase {
    public PyxyParsingTest() {
        super("", "pyxy", new PyxyParserDefinition());
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected String getTestDataPath() {
        return "./testdata/";
    }

    @Override
    protected boolean skipSpaces() {
        return false;
    }

    @Override
    protected boolean includeRanges() {
        return true;
    }

    public void testBasic() {
        doTest(true);
    }
}
