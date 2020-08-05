package cn.javaer.snippets.spring.format;

import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.datetime.standard.TemporalAccessorPrinter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

/**
 * @author cn-src
 */
public class DateFillFormatter extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<DateFillFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return Collections.singleton(LocalDateTime.class);
    }

    @Override
    public Printer<?> getPrinter(final DateFillFormat annotation, final Class<?> fieldType) {
        return new TemporalAccessorPrinter(DateTimeFormatter.ofPattern(annotation.dataTimePattern()));
    }

    @Override
    public Parser<?> getParser(final DateFillFormat annotation, final Class<?> fieldType) {
        return new DateFillParser(annotation);
    }
}
