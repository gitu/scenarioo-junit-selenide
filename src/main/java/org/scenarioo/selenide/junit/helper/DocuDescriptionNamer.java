package org.scenarioo.selenide.junit.helper;

import org.scenarioo.selenide.junit.DocuDescription;
import org.apache.commons.lang3.StringUtils;
import org.scenarioo.model.docu.entities.UseCase;

public class DocuDescriptionNamer {

    public static UseCase createUseCase(final Class<?> testClass) {
        // Extract usecase name and description from concrete test class.
        String description = "";
        String name = createUseCaseName(testClass);
        DocuDescription docuDescription = testClass.getAnnotation(DocuDescription.class);
        if (docuDescription != null) {
            description = docuDescription.description();
        }
        // Create use case
        UseCase useCase = new UseCase();
        useCase.setName(name);
        useCase.setDescription(description);
        useCase.addDetail("webtestClass", testClass.getName());
        return useCase;
    }

    public static String createUseCaseName(final Class<?> testClass) {
        DocuDescription description = testClass.getAnnotation(DocuDescription.class);
        if (description != null && !StringUtils.isBlank(description.name())) {
            return description.name();
        } else {
            // simply use the test class name as use case name if not set through description annotation.
            return testClass.getSimpleName();
        }
    }
}
