package cn.javaer.snippets.jackson;

import cn.javaer.snippets.format.DateMaxTime;
import cn.javaer.snippets.format.DateMinTime;
import cn.javaer.snippets.format.DateTimeFormat;
import cn.javaer.snippets.util.ReflectionUtils;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.util.Optional;

/**
 * @author cn-src
 */
public class DateTimeFormatIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = -6156647757687961666L;

    public static final DateTimeFormatIntrospector INSTANCE = new DateTimeFormatIntrospector();

    @Override
    public Object findDeserializer(final Annotated a) {
        final DateTimeFormat format = ReflectionUtils.getAnnotation(DateTimeFormat.class,
            a.getAnnotation(DateTimeFormat.class),
            a.getAnnotation(DateMinTime.class),
            a.getAnnotation(DateMaxTime.class)
        );
        return Optional.ofNullable(format)
            .map(it -> (Object) new DateTimeFormatDeserializer(it))
            .orElse(super.findDeserializer(a));
    }
}