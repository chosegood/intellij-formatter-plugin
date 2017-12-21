import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;

public class FormatTest extends UsefulTestCase {

    protected CodeInsightTestFixture myFixture;

    final String dataPath = Paths.get("src/test/resource/").toAbsolutePath().toString();

    @Before
    public void setUp() throws Exception {
        final IdeaTestFixtureFactory fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> testFixtureBuilder = fixtureFactory.createFixtureBuilder(getName());
        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(testFixtureBuilder.getFixture());
        myFixture.setTestDataPath(dataPath);
        final JavaModuleFixtureBuilder builder = testFixtureBuilder.addModule(JavaModuleFixtureBuilder.class);

        builder.addContentRoot(myFixture.getTempDirPath()).addSourceRoot("");
        builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15);
        myFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        myFixture.tearDown();
        myFixture = null;
    }

    @Test
    public void testDefinition() throws Exception {
        myFixture.configureByFile("definition.before.template.java");

        final IntentionAction action = myFixture.findSingleIntention("Format parameters");
        assertNotNull(action);

        myFixture.launchAction(action);
        myFixture.checkResultByFile("definition.before.template.after.java");
    }

    @Test
    public void testInvocation() throws Exception {
        myFixture.configureByFile("invocation.before.template.java");

        final IntentionAction action = myFixture.findSingleIntention("Format parameters");
        assertNotNull(action);

        myFixture.launchAction(action);
        myFixture.checkResultByFile("invocation.before.template.after.java");
    }
}
