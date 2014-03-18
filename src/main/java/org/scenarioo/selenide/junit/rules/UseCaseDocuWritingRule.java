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

import org.apache.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.UseCase;

import static org.scenarioo.selenide.junit.helper.DocuDescriptionNamer.createUseCase;
import static org.scenarioo.selenide.junit.ScenariooDocuConfig.*;

/**
 * A {@link org.junit.rules.TestRule} to setup as a static {@link org.junit.ClassRule} on your UI test classes to generate documentation content
 * for each running test class as a Usecase inside the Scenarioo Documentation.
 */
public class UseCaseDocuWritingRule implements TestRule {

    private static final Logger LOGGER = Logger.getLogger(UseCaseDocuWritingRule.class);

    @Override
    public Statement apply(final Statement test, final Description testClassDescription) {

        // Statement to write use case description as soon as test class gets executed
        return new Statement() {

            private final ScenarioDocuWriter docuWriter = new ScenarioDocuWriter(DOCU_BUILD_DIRECTORY, BRANCH_NAME, BUILD_NAME);

            @Override
            public void evaluate() throws Throwable {
                if (SCENARIOO_ENABLED) {
                    try {
                        // Save use case description
                        UseCase useCase = createUseCase(testClassDescription.getTestClass());
                        LOGGER.info("Generating Scenarioo Docu for UseCase " + useCase.getName() + ": "
                                + useCase.getDescription());
                        docuWriter.saveUseCase(useCase);
                        // Run tests
                        test.evaluate();
                    } finally {
                        // Wait until asynch writing has finished.
                        docuWriter.flush();
                    }
                } else {
                    test.evaluate();
                }
            }
        };
    }


}
