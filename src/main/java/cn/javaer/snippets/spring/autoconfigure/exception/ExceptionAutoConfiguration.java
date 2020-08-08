package cn.javaer.snippets.spring.autoconfigure.exception;

import cn.javaer.snippets.spring.exception.GlobalExceptionAdvice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ExceptionMappingProperties.class, ServerProperties.class})
@ConditionalOnProperty(prefix = "snippets.exception", name = "enabled", havingValue = "true",
        matchIfMissing = true)
public class ExceptionAutoConfiguration implements InitializingBean {
    private final ExceptionMappingProperties exceptionMappingProperties;
    private final ServerProperties serverProperties;
    private Map<String, GlobalExceptionAdvice.ErrorStatus> useMapping;

    public ExceptionAutoConfiguration(
            final ExceptionMappingProperties exceptionMappingProperties,
            final ServerProperties serverProperties) {
        this.exceptionMappingProperties = exceptionMappingProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    ResourceBundleMessageSource errorsMessageSource() {
        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setDefaultEncoding("UTF-8");
        source.setBasenames("errors-messages", "default-errors-messages");
        return source;
    }

    @Bean
    GlobalExceptionAdvice globalExceptionAdvice(final ResourceBundleMessageSource messageSource) {
        return new GlobalExceptionAdvice(this.serverProperties.getError(), this.useMapping,
                messageSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Map<String, String> mapping = this.exceptionMappingProperties.getMapping();

        if (!CollectionUtils.isEmpty(mapping)) {
            this.useMapping = new HashMap<>(mapping.size());
            for (final Map.Entry<String, String> entry : mapping.entrySet()) {
                final String value = entry.getValue();
                if (value != null) {
                    final int i = value.indexOf(',');
                    if (i > 0) {
                        final GlobalExceptionAdvice.ErrorStatus errorStatus =
                                new GlobalExceptionAdvice.ErrorStatus(Integer.parseInt(value.substring(0,
                                        i)), value.substring(i + 1));
                        this.useMapping.put(entry.getKey(), errorStatus);
                    }
                    else {
                        throw new InvalidConfigurationPropertyValueException(entry.getKey(),
                                entry.getValue(), "Missing status or error");
                    }
                }
            }
        }
    }
}
