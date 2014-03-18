/* Copyright (c) 2014, scenarioo.org Development Team
 * All rights reserved.
 *
 * See https://github.com/scenarioo?tab=members
 * for a complete list of contributors to this project.
 *
 * Redistribution and use of the Scenarioo Examples in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.scenarioo.selenide.junit.rules;

import org.scenarioo.selenide.junit.DocuDescription;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;

import static org.scenarioo.selenide.junit.helper.DocuDescriptionNamer.createUseCase;
import static org.scenarioo.selenide.junit.ScenariooDocuConfig.*;

/**
 * A {@link org.junit.rules.TestRule} to setup as a {@link org.junit.Rule} on your UI test classes to generate documentation content for each
 * running test method as a {@link org.scenarioo.model.docu.entities.Scenario} inside the Scenarioo Documentation.
 */
public class ScenarioDocuWritingRule extends TestWatcher {

    private static final Logger LOGGER = Logger.getLogger(UseCaseDocuWritingRule.class);

    private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, BRANCH_NAME, BUILD_NAME);

    private UseCase useCase;

    private Scenario scenario;

    /**
     * Initialize current running usecase and scenario before the test gets executed.
     */
    @Override
    protected void starting(final Description testMethodDescription) {
        useCase = createUseCase(testMethodDescription.getTestClass());
        scenario = createScenario(testMethodDescription);
    }

    private Scenario createScenario(final Description testMethodDescription) {
        Scenario scenario = new Scenario();
        String description = "";
        String name = createScenarioName(testMethodDescription);
        DocuDescription docuDescription = testMethodDescription.getAnnotation(DocuDescription.class);
        if (docuDescription != null) {
            description = docuDescription.description();
            scenario.addDetail("userRole", docuDescription.userRole());
        }
        scenario.setName(name);
        scenario.setDescription(description);
        return scenario;
    }

    private String createScenarioName(final Description testMethodDescription) {
        DocuDescription description = testMethodDescription.getAnnotation(DocuDescription.class);
        if (description != null && !StringUtils.isBlank(description.name())) {
            return description.name();
        } else {
            // simply use the test name as scenario name if not set through description annotation.
            return testMethodDescription.getMethodName();
        }
    }

    /**
     * When test succeeded: Save the scenario with status 'success'
     */
    @Override
    protected void succeeded(final Description description) {
        writeScenarioDescription("success");
    }

    /**
     * When test failed: Save the scenario with status 'failed'
     */
    @Override
    protected void failed(final Throwable e, final Description description) {
        writeScenarioDescription("failed");
    }

    private void writeScenarioDescription(final String status) {

        // Write scenario
        LOGGER.info("Generating Scenarioo Docu for Scenario " + useCase.getName() + "." + scenario.getName() + " ("
                + status + ") : " + scenario.getDescription());
        scenario.setStatus(status);
        docuWriter.saveScenario(useCase, scenario);

        // Wait until asynch writing has finished.
        docuWriter.flush();
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
