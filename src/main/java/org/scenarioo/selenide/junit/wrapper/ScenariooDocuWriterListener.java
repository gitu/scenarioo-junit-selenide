package org.scenarioo.selenide.junit.wrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.scenarioo.api.ScenarioDocuWriter;

public class ScenariooDocuWriterListener implements WebDriverEventListener {

    private final ScenariooDocuWriterWrapper docuWrapper;


    public ScenariooDocuWriterListener(ScenarioDocuWriter docuWriter) {
        this.docuWrapper = new ScenariooDocuWriterWrapper(docuWriter);
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        docuWrapper.saveStepWithScreenshotSuccess("afterNavigateTo");
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
        docuWrapper.saveStepWithScreenshotSuccess("afterNavigateBack");
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
        docuWrapper.saveStepWithScreenshotSuccess("afterNavigateForward");
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        docuWrapper.saveStepWithScreenshotSuccess("afterClickOn");
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver driver) {

    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver driver) {
        docuWrapper.saveStepWithScreenshotSuccess("afterChangeValueOf");
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {

    }

    @Override
    public void afterScript(String script, WebDriver driver) {
        docuWrapper.saveStepWithScreenshotSuccess("afterScript");
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        docuWrapper.saveStepWithScreenshotFailed("onException");
    }
}
