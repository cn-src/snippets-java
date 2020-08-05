package cn.javaer.snippets.spring.format;

import org.springframework.format.Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author cn-src
 */
public final class DateFillParser implements Parser<LocalDateTime> {
    private final DateFillFormat annotation;

    public DateFillParser(final DateFillFormat annotation) {
        this.annotation = annotation;
    }

    @Override
    public LocalDateTime parse(final String text, final Locale locale) {
        if (this.annotation.dataPattern().length() == text.length()) {
            final LocalDate date = LocalDate.parse(text,
                    DateTimeFormatter.ofPattern(this.annotation.dataPattern(), locale));
            return DateFillFormat.Conversion.conversion(date, this.annotation);
        }
        final LocalDateTime dateTime = LocalDateTime.parse(text,
                DateTimeFormatter.ofPattern(this.annotation.dataPattern(), locale));
        return DateFillFormat.Conversion.conversion(dateTime, this.annotation);
    }
}
