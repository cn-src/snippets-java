package cn.javaer.snippets.jackson;

import cn.javaer.snippets.spring.format.DateFillFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author cn-src
 */
public class DateFillDeserializer extends JsonDeserializer<LocalDateTime> implements ContextualDeserializer {

    private final DateFillFormat dateFillFormat;
    private final DateTimeFormatter formatter;

    protected DateFillDeserializer() {
        this.dateFillFormat = null;
        this.formatter = null;
    }

    public DateFillDeserializer(final DateFillFormat dateFillFormat) {
        Objects.requireNonNull(dateFillFormat);
        this.dateFillFormat = dateFillFormat;
        this.formatter = DateTimeFormatter.ofPattern(dateFillFormat.dataPattern());
    }

    @Override
    public LocalDateTime deserialize(final JsonParser parser,
                                     final DeserializationContext context) throws IOException {
        if (this.dateFillFormat.dataPattern().length() == parser.getText().length()) {
            final LocalDate date = LocalDate.parse(parser.getText(), this.formatter);
            return DateFillFormat.Conversion.conversion(date, this.dateFillFormat);
        }
        else {
            final LocalDateTime dateTime = LocalDateTime.parse(parser.getText(), this.formatter);
            return DateFillFormat.Conversion.conversion(dateTime, this.dateFillFormat);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context,
                                                final BeanProperty property) {
        final AnnotatedElement annotated = property.getMember().getAnnotated();
        DateFillFormat dateFillFormat = annotated.getAnnotation(DateFillFormat.class);
        if (null == dateFillFormat) {
            dateFillFormat = property.getAnnotation(DateFillFormat.class);
        }
        return new DateFillDeserializer(dateFillFormat);
    }
}
