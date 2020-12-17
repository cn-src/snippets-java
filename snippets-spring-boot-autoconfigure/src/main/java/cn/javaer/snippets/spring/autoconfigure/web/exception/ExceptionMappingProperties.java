package cn.javaer.snippets.spring.autoconfigure.web.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author cn-src
 */
@Data
@ConfigurationProperties(prefix = "snippets.web.exception")
public class ExceptionMappingProperties {
    private Map<String, String> mapping;
}
