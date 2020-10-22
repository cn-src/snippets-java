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
 * @see MergedAnnotations
 * @see org.springframework.core.annotation.AnnotatedElementUtils
 */
public interface AnnotationUtils {

    /**
     * 使用 spring 的方式合并注解.
     *
     * @param annotationClass 最终注解的类型
     * @param annotations 要合并的注解
     * @param <A> 最终注解的类型
     *
     * @return 最终注解
     */
    static <A extends Annotation> Optional<A> mergeAnnotations(final Class<A> annotationClass,
                                                               final Annotation... annotations) {

        return MergedAnnotations.from(annotations)
            .get(annotationClass, null, MergedAnnotationSelectors.firstDirectlyDeclared())
            .synthesize(MergedAnnotation::isPresent);
    }

    /**
     * 使用 spring 的方式合并注解.
     *
     * @param annotationClass 最终注解的类型
     * @param fun 要合并的注解的获取函数
     * @param <A> 最终注解的类型
     *
     * @return 最终注解
     */
    @SafeVarargs
    static <A extends Annotation> Optional<A> mergeAnnotations(final Class<A> annotationClass,
                                                               final Supplier<Annotation>... fun) {

        return mergeAnnotations(annotationClass,
            Arrays.stream(fun).map(Supplier::get).toArray(Annotation[]::new));
    }
}
