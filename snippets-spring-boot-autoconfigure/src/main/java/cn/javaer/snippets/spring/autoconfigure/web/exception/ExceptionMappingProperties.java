package cn.javaer.snippets.spring.autoconfigure.web.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author cn-src
 */
@Data
@ConfigurationProperties(prefix = "snippets.web.exception")
public class ExceptionMappingProperties {

    private Map<String, Error> mapping;

    @Data
    public static class Error {
        @NotEmpty
        private String error;
        @NotNull
        private Integer status;
    }
}
