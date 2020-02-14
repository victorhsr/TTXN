package io.github.victorhsr.ttxn.commons.reflection;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * Util class for reflection operations
 *
 * @author victorhsr <victor.hugo.origins@gmail.com>
 */
public class Reflections {

    /**
     * Tries to retrieve an {@link Integer} that indicates which parameter contains the searched annotation
     * @param parameterAnnotations parameter annotations to verify
     * @param annotationClass annotation that we are looking for
     * @return case exists, the parameter index that is annotated
     * @throws MoreThanOneParameterAnnotedException if more than one parameter implements the annotation
     */
    public static Optional<Integer> findParamsAnnotationIndex(final Annotation[][] parameterAnnotations, final Class<? extends Annotation> annotationClass) throws MoreThanOneParameterAnnotedException {

        Integer parameterIndex = null;
        for (int index = 0; index < parameterAnnotations.length; index++) {
            for (Annotation annotation : parameterAnnotations[index]) {
                if (annotationClass.isInstance(annotation)) {
                    if (Objects.nonNull(parameterIndex)) {
                        throw new MoreThanOneParameterAnnotedException(annotationClass);
                    }
                    parameterIndex = index;
                }
            }
        }

        return Optional.ofNullable(parameterIndex);
    }
}
