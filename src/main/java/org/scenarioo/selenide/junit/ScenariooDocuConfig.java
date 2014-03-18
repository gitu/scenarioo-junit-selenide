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

package org.scenarioo.selenide.junit;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Just some constants for the example UI test documentation generation.
 */
public class ScenariooDocuConfig {

    private static final Logger LOGGER = Logger.getLogger(ScenariooDocuConfig.class);


    public static final boolean SCENARIOO_ENABLED = Boolean.parseBoolean(System.getProperty("scenarioo.enabled", "false"));
    public static final File DOCU_BUILD_DIRECTORY = new File(System.getProperty("scenarioo.docu_build.dir", "build/scenarioo"));
    public static final String BRANCH_NAME = System.getProperty("scenarioo.branch", "master");
    public static final String BUILD_NAME = System.getProperty("scenarioo.build", "develop");
    public static final File TEST_SRC_DIRECTORY = new File(System.getProperty("scenarioo.test_src.dir", "src/test/java"));

    static {
        // Ensure that the build directory gets precreated, this is not handled by the Scenarioo Docu writer, if
        // directory does not exist docu generation will fail.
        if (!SCENARIOO_ENABLED) {
            boolean mkdirsResult = DOCU_BUILD_DIRECTORY.mkdirs();
            LOGGER.debug("result of creating directory " + DOCU_BUILD_DIRECTORY + " - " + mkdirsResult);
        }
    }

}
