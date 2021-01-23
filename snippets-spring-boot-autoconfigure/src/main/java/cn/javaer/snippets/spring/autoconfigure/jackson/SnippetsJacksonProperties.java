package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.util.TimeUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author cn-src
 */
@Data
@ConfigurationProperties(prefix = "snippets.jackson")
public class SnippetsJacksonProperties {

    private Format format = new Format();

    @Data
    public static class Format {

        /**
         * Date format to use, for example `yyyy-MM-dd`.
         */
        private String date = TimeUtils.DATE_PATTERN;

        /**
         * Time format to use, for example `HH:mm:ss`.
         */
        private String time = TimeUtils.TIME_PATTERN;

        /**
         * Date-time format to use, for example `yyyy-MM-dd HH:mm:ss`.
         */
        private String dateTime = TimeUtils.DATE_TIME_PATTERN;
    }
}
