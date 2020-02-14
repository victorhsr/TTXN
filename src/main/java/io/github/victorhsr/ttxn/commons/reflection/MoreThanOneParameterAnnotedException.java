package io.github.victorhsr.ttxn.commons.reflection;

import java.lang.annotation.Annotation;

/**
 * Exception thrown when more than one parameter of method use the same annotation
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
public class MoreThanOneParameterAnnotedException extends RuntimeException {

    public MoreThanOneParameterAnnotedException(final Class<? extends Annotation> annotationClass) {
        super(String.format("More than one parameter is annoted with %s", annotationClass.getName()));
    }
}
