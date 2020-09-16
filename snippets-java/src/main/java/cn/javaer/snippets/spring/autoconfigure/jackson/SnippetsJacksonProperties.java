package cn.javaer.snippets.spring.autoconfigure.jackson;

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
        private String date = "yyyy-MM-dd";

        /**
         * Time format to use, for example `HH:mm:ss`.
         */
        private String time = "HH:mm:ss";

        /**
         * Date-time format to use, for example `yyyy-MM-dd HH:mm:ss`.
         */
        private String dateTime = "yyyy-MM-dd HH:mm:ss";
    }
}
