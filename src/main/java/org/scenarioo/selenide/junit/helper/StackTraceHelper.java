package org.scenarioo.selenide.junit.helper;

import org.scenarioo.selenide.junit.ScenariooDocuConfig;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class StackTraceHelper<T extends Annotation, S extends Annotation> {
    private static final Logger LOGGER = Logger.getLogger(StackTraceHelper.class);

    private final Class<T> classAnnotation;
    private final Class<S> methodAnnotation;

    public StackTraceHelper(Class<T> classAnnotation, Class<S> methodAnnotation) {
        this.classAnnotation = classAnnotation;
        this.methodAnnotation = methodAnnotation;
    }

    public T getClassAnnotation(StackTraceElement element) {
        try {
            Class<?> clazz = Class.forName(element.getClassName());
            if (clazz.isAnnotationPresent(this.classAnnotation)) {
                return clazz.getAnnotation(this.classAnnotation);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.warn("class not found, which is weird...", e);
        }
        return null;
    }

    public S getMethodAnnotation(StackTraceElement element) {
        try {
            Class<?> clazz = Class.forName(element.getClassName());
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(element.getMethodName())) {
                    if (method.isAnnotationPresent(this.methodAnnotation)) {
                        return method.getAnnotation(this.methodAnnotation);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.warn("class not found, which is weird...", e);
        }
        return null;
    }

    public StackTraceElement getAnnotatedStackElement() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            try {
                Class<?> clazz = Class.forName(element.getClassName());
                if (clazz.isAnnotationPresent(this.classAnnotation)) {
                    return element;
                }
            } catch (ClassNotFoundException e) {
                LOGGER.warn("class not found, which is weird...", e);
            }
        }
        return null;
    }

    public String getSourceLine(StackTraceElement element) {
        File file = new File(ScenariooDocuConfig.TEST_SRC_DIRECTORY, element.getClassName().replace(".", File.separator) + ".java");
        if (file.exists()) {
            try {
                return String.valueOf(FileUtils.readLines(file).get(element.getLineNumber() - 1));
            } catch (IOException e) {
                LOGGER.warn("we had a problem reading the source file " + file.toString(), e);
                return null;
            }
        } else {
            LOGGER.debug("file does not exist: " + file.getAbsolutePath());
        }
        return null;
    }

}
