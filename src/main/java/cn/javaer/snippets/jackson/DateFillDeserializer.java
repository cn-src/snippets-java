package cn.javaer.snippets.jackson;

import cn.javaer.snippets.spring.format.DateFillFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.springframework.core.annotation.AnnotatedElementUtils;

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

    protected DateFillDeserializer() {
        this.dateFillFormat = null;
    }

    public DateFillDeserializer(final DateFillFormat dateFillFormat) {
        Objects.requireNonNull(dateFillFormat);
        this.dateFillFormat = dateFillFormat;
    }

    @Override
    public LocalDateTime deserialize(final JsonParser parser,
                                     final DeserializationContext context) throws IOException {
        Objects.requireNonNull(this.dateFillFormat);
        if (this.dateFillFormat.datePattern().length() == parser.getText().length()) {
            final LocalDate date = LocalDate.parse(parser.getText(),
                DateTimeFormatter.ofPattern(this.dateFillFormat.datePattern()));
            return DateFillFormat.Conversion.conversion(date, this.dateFillFormat);
        }
        final LocalDateTime dateTime = LocalDateTime.parse(parser.getText(),
            DateTimeFormatter.ofPattern(this.dateFillFormat.dateTimePattern()));
        return DateFillFormat.Conversion.conversion(dateTime, this.dateFillFormat);
    }

    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context,
                                                final BeanProperty property) {
        final AnnotatedElement element = property.getMember().getAnnotated();
        if (element == null) {
            return null;
        }
        final DateFillFormat dateFillFormat =
            AnnotatedElementUtils.findMergedAnnotation(element, DateFillFormat.class);
        if (null == dateFillFormat) {
            return null;
        }
        return new DateFillDeserializer(dateFillFormat);
    }
}
