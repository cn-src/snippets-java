package cn.javaer.snippets.jackson;

import cn.javaer.snippets.format.DateMaxTime;
import cn.javaer.snippets.format.DateMinTime;
import cn.javaer.snippets.format.DateTimeFormat;
import cn.javaer.snippets.util.AnnotationUtils;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author cn-src
 */
public class SnippetsJacksonIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = -6156647757687961666L;

    public static final SnippetsJacksonIntrospector INSTANCE = new SnippetsJacksonIntrospector();

    @Override
    public Object findDeserializer(final Annotated a) {

        return AnnotationUtils.mergeAnnotations(DateTimeFormat.class,
            () -> a.getAnnotation(DateTimeFormat.class),
            () -> a.getAnnotation(DateMinTime.class),
            () -> a.getAnnotation(DateMaxTime.class)
        ).map(it -> (Object) new DateTimeFormatDeserializer(it)).orElse(super.findDeserializer(a));
    }
}
