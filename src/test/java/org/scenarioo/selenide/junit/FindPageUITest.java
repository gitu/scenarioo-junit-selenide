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

import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

/**
 * An example of how to use the Scenarioo API to generate User Scenario Documentation from your UI tests.
 * 
 * The structure of the tests assumes that one test class corresponds to the use cases and one test case corresponds to
 * a scenario of this use cases, which is a good practice to follow for structuring your UI tests and documenting your
 * software by user scenarios.
 * 
 * The example also demonstrates how to hide the writing of the documentation inside your testing infrastructure, see
 * the base infrastructure testing classes on how the documentation writing is done: {@link UITest},
 * {@link org.scenarioo.selenide.junit.rules.UseCaseDocuWritingRule}, {@link org.scenarioo.selenide.junit.rules.ScenarioDocuWritingRule}.
 * 
 * Hint for real implementation of your UI tests: In a real application we would recommend to use some additional
 * patterns to structure your probably more complex webtests: Instead of directly programming your tests against the
 * abstracted toolkit, we recommend to use the "PageObject" pattern and to introduce page components such that you can
 * assemble your page objects simply from some reusable page components (see also Composite pattern). We did not
 * implement these patterns for this simple example.
 */
@DocuDescription(name = "Find Page", description = "User wants to search for a page and read it.")
public class FindPageUITest extends UITest {
	
	@Test
	@DocuDescription(description = "User enters some text and finds multiple pages that contain this text.")
	public void find_page_with_text_on_page_from_multiple_results() {
		open("http://www.wikipedia.org");
        $("#searchInput").setValue("best band in the world");
        $("input.formBtn").click();
        $(".mw-search-results").should(exist);
        $(By.linkText("U2")).should(exist);
        $(By.linkText("U2")).click();
	}

	@Test
	@DocuDescription(description = "User enters exact page title and finds it directly.")
	public void find_page_with_title_direct() {
        open("http://www.wikipedia.org");
        $("#searchInput").setValue("FC Basel");
        $("input.formBtn").click();
        $("#mw-content-text").shouldHave(text("Swiss football club"));
	}
	
	@Test
	@DocuDescription(
			description = "User enters page title that is ambiguous but matches directly a page, on the page he sees the list of other meanings, and can navigate to the page he meant.")
	public void find_page_with_title_ambiguous_navigate_to_other_meaning() {
        open("http://www.wikipedia.org");
        $("#searchInput").setValue("42");
        $("input.formBtn").click();
        $(By.linkText("42 (number)")).click();
        $(By.linkText("Answer to The Ultimate Question of Life, the Universe, and Everything")).click();
	}
	
	@DocuDescription(
			description = "User enters text that is not found in pages content.")
	@Test
	public void find_page_no_result() {
        open("http://www.wikipedia.org");
        $("#searchInput").setValue("Scenarioo");
        $("input.formBtn").click();
		$("#mw-content-text").shouldHave(text("There were no results"));
        $("#mw-content-text").shouldHave(text("Did you mean: scenario"));
	}



}
