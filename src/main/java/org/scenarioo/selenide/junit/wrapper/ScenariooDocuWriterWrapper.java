package org.scenarioo.selenide.junit.wrapper;

import com.codeborne.selenide.WebDriverRunner;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.scenarioo.api.ScenarioDocuWriter;
import org.scenarioo.model.docu.entities.*;
import org.scenarioo.selenide.junit.DocuDescription;
import org.scenarioo.selenide.junit.helper.StackTraceHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.annotation.IncompleteAnnotationException;

public class ScenariooDocuWriterWrapper {
    private final ScenarioDocuWriter docuWriter;
    private int stepIndex = 0;

    private final StackTraceHelper<DocuDescription, DocuDescription> docuDescriptionStackTraceHelper
            = new StackTraceHelper<DocuDescription, DocuDescription>(DocuDescription.class, DocuDescription.class);

    public ScenariooDocuWriterWrapper(ScenarioDocuWriter docuWriter) {
        this.docuWriter = docuWriter;
    }

    public void saveStepWithScreenshotSuccess(String trigger) {
        saveStepWithScreenshot("sucess", trigger);
    }

    public void saveStepWithScreenshotFailed(String trigger) {
        saveStepWithScreenshot("failed", trigger);
    }

    public void saveStepWithScreenshot(final String status, String trigger) {
        StackTraceElement element = docuDescriptionStackTraceHelper.getAnnotatedStackElement();

        if (element == null) {
            throw new IncompleteAnnotationException(DocuDescription.class, "DocuDescription Annotation should be set on your test class & method.");
        }

        DocuDescription classAnnotation = docuDescriptionStackTraceHelper.getClassAnnotation(element);
        DocuDescription methodAnnotation = docuDescriptionStackTraceHelper.getMethodAnnotation(element);

        if ((classAnnotation) == null)
            throw new IncompleteAnnotationException(DocuDescription.class, "DocuDescription Annotation should be set on your test class.");
        if ((methodAnnotation) == null)
            throw new IncompleteAnnotationException(DocuDescription.class, "DocuDescription Annotation should be set on your test method.");


        String useCaseName = classAnnotation.name();
        String scenarioName = StringUtils.isNotBlank(methodAnnotation.name()) ? methodAnnotation.name() : element.getMethodName();
        Step step = createStep(status, useCaseName, scenarioName, stepIndex, element, trigger);
        docuWriter.saveStep(useCaseName, scenarioName, step);

        String screnshotFilePath = useCaseName + "." + scenarioName + "." + stepIndex;
        String fullScreenshotFilePath = WebDriverRunner.takeScreenShot(screnshotFilePath);
        docuWriter.saveScreenshot(useCaseName, scenarioName, stepIndex, getScreenshotAsString(fullScreenshotFilePath));

        // increase step index
        stepIndex++;
    }

    private String getScreenshotAsString(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            final byte[] encodedScreenshot = Base64.encodeBase64(baos.toByteArray());
            return new String(encodedScreenshot);
        } catch (Exception e) {
            throw new RuntimeException("Could not read image: " + filename, e);
        }
    }


    private Step createStep(final String status, String useCase, String scenario, int stepIndex, StackTraceElement element, String trigger) {
        Step step = new Step();
        step.setStepDescription(createStepDescription(status, useCase, scenario, stepIndex));
        step.setPage(createPage());
        step.setMetadata(createStepMetadata(element, trigger));
        step.setHtml(new StepHtml(WebDriverRunner.source()));
        return step;
    }


    private Page createPage() {
        return new Page(WebDriverRunner.getWebDriver().getTitle());
    }


    private StepDescription createStepDescription(final String status, String useCase, String scenario, int stepIndex) {
        StepDescription stepDescription = new StepDescription();
        stepDescription.setTitle(WebDriverRunner.getWebDriver().getTitle());
        stepDescription.setStatus(status);
        stepDescription.setIndex(stepIndex);
        stepDescription.addDetails("url", WebDriverRunner.url());
        stepDescription.setScreenshotFileName(docuWriter.getScreenshotFile(useCase, scenario, stepIndex).getName());
        return stepDescription;
    }

    private StepMetadata createStepMetadata(StackTraceElement element, String trigger) {
        StepMetadata metadata = new StepMetadata();
        metadata.addDetail("Source", element.toString());
        metadata.addDetail("Statement", docuDescriptionStackTraceHelper.getSourceLine(element));
        metadata.addDetail("Trigger", trigger);
        return metadata;
    }
}
