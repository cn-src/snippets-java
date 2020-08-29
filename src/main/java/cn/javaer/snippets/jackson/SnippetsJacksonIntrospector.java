package cn.javaer.snippets.jackson;

import cn.javaer.snippets.spring.format.DateFillFormat;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.AnnotatedElement;

/**
 * @author cn-src
 */
public class SnippetsJacksonIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = -6156647757687961666L;

    public static final SnippetsJacksonIntrospector INSTANCE = new SnippetsJacksonIntrospector();

    @Override
    public Object findDeserializer(final Annotated a) {
        final AnnotatedElement element = a.getAnnotated();
        if (element != null) {
            final DateFillFormat fillFormat =
                AnnotatedElementUtils.findMergedAnnotation(element, DateFillFormat.class);
            if (null != fillFormat) {
                return new DateFillDeserializer(fillFormat);
            }
        }
        return super.findDeserializer(a);
    }
}
