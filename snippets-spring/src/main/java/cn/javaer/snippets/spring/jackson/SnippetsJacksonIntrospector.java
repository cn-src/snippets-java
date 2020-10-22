package cn.javaer.snippets.spring.jackson;

import cn.javaer.snippets.spring.AnnotationUtils;
import cn.javaer.snippets.spring.format.DateFillFormat;
import cn.javaer.snippets.spring.format.DateMaxTime;
import cn.javaer.snippets.spring.format.DateMinTime;
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

        return AnnotationUtils.mergeAnnotations(DateFillFormat.class,
            () -> a.getAnnotation(DateFillFormat.class),
            () -> a.getAnnotation(DateMinTime.class),
            () -> a.getAnnotation(DateMaxTime.class)
        ).map(it -> (Object) new DateFillDeserializer(it)).orElse(super.findDeserializer(a));
    }
}
