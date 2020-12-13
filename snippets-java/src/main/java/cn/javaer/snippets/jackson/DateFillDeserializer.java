package cn.javaer.snippets.jackson;

import cn.javaer.snippets.format.DateMaxTime;
import cn.javaer.snippets.format.DateMinTime;
import cn.javaer.snippets.format.DateTimeFormat;
import cn.javaer.snippets.util.AnnotationUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author cn-src
 */
public class DateFillDeserializer extends JsonDeserializer<LocalDateTime> implements ContextualDeserializer {

    private final DateTimeFormat dateTimeFormat;

    protected DateFillDeserializer() {
        this.dateTimeFormat = null;
    }

    public DateFillDeserializer(final DateTimeFormat dateTimeFormat) {
        Objects.requireNonNull(dateTimeFormat);
        this.dateTimeFormat = dateTimeFormat;
    }

    @Override
    public LocalDateTime deserialize(final JsonParser parser,
                                     final DeserializationContext context) throws IOException {
        Objects.requireNonNull(this.dateTimeFormat);
        if (this.dateTimeFormat.datePattern().length() == parser.getText().length()) {
            final LocalDate date = LocalDate.parse(parser.getText(),
                DateTimeFormatter.ofPattern(this.dateTimeFormat.datePattern()));
            return DateTimeFormat.Conversion.conversion(date, this.dateTimeFormat);
        }
        final LocalDateTime dateTime = LocalDateTime.parse(parser.getText(),
            DateTimeFormatter.ofPattern(this.dateTimeFormat.dateTimePattern()));
        return DateTimeFormat.Conversion.conversion(dateTime, this.dateTimeFormat);
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context,
                                                final BeanProperty property) {

        return AnnotationUtils.mergeAnnotations(DateTimeFormat.class,
            () -> property.getAnnotation(DateTimeFormat.class),
            () -> property.getAnnotation(DateMinTime.class),
            () -> property.getAnnotation(DateMaxTime.class)
        ).map(DateFillDeserializer::new).orElse(null);
    }
}
