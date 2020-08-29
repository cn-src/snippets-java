package cn.javaer.snippets.spring;

import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotationSelectors;
import org.springframework.core.annotation.MergedAnnotations;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author cn-src
 */
public interface AnnotationUtils {
    static <A extends Annotation> Optional<A> mergeAnnotations(final Class<A> annotationClass,
                                                               final Annotation... annotations) {

        return MergedAnnotations.from(annotations)
            .get(annotationClass, null, MergedAnnotationSelectors.firstDirectlyDeclared())
            .synthesize(MergedAnnotation::isPresent);
    }

    @SafeVarargs
    static <A extends Annotation> Optional<A> mergeAnnotations(final Class<A> annotationClass,
                                                               final Supplier<Annotation>... fun) {

        return mergeAnnotations(annotationClass,
            Arrays.stream(fun).map(Supplier::get).toArray(Annotation[]::new));
    }
}
