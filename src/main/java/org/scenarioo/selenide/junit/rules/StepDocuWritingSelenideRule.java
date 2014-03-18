package org.scenarioo.selenide.junit.rules;

import static org.scenarioo.selenide.junit.ScenariooDocuConfig.*;

import org.scenarioo.selenide.junit.wrapper.ScenariooDocuWriterListener;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.scenarioo.api.ScenarioDocuWriter;

/**
 * A {@link org.junit.rules.TestRule} to setup as a static {@link org.junit.ClassRule} on your UI test classes to generate documentation content
 * for each running Test adds the Step inside the Scenarioo Documentation.
 */
public class StepDocuWritingSelenideRule extends ExternalResource {

    private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, BRANCH_NAME, BUILD_NAME);
    private final ScenariooDocuWriterListener stepWriter = new ScenariooDocuWriterListener(docuWriter);


    @Override
    protected void before() throws Throwable {
        WebDriverRunner.addListener(stepWriter);
    }

    @Override
    protected void after() {
        WebDriverRunner.closeWebDriver();
    }


    @Override
    public Statement apply(final Statement test, final Description testClassDescription) {
        if (SCENARIOO_ENABLED) {
            return super.apply(test, testClassDescription);
        } else {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    test.evaluate();
                }
            };
        }
    }
}
