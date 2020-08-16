package cn.javaer.snippets.spring.autoconfigure.exception;

import cn.javaer.snippets.spring.web.exception.DefinedErrorInfo;
import cn.javaer.snippets.spring.web.exception.ErrorInfoController;
import cn.javaer.snippets.spring.web.exception.ErrorInfoExtractor;
import cn.javaer.snippets.spring.web.exception.GlobalExceptionAdvice;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

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
    private Map<String, DefinedErrorInfo> useMapping;

    public ExceptionAutoConfiguration(
            final ExceptionMappingProperties exceptionMappingProperties,
            final ServerProperties serverProperties) {
        this.exceptionMappingProperties = exceptionMappingProperties;
        this.serverProperties = serverProperties;
    }

    @Bean
    @Lazy
    ErrorInfoController errorInfoController(final ErrorInfoExtractor errorInfoExtractor) {
        return new ErrorInfoController(errorInfoExtractor);
    }

    @Bean
    ResourceBundleMessageSource errorsMessageSource() {
        final ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setDefaultEncoding("UTF-8");
        try {
            ResourceBundle.getBundle("errors-messages");
            source.setBasenames("errors-messages", "default-errors-messages");
        }
        catch (final MissingResourceException ignore) {
            source.setBasenames("default-errors-messages");
        }
        return source;
    }

    @Bean
    ErrorInfoExtractor errorInfoExtractor(final ResourceBundleMessageSource messageSource) {
        return new ErrorInfoExtractor(this.useMapping, messageSource,
                this.serverProperties.getError().getIncludeMessage() == ErrorProperties.IncludeAttribute.ALWAYS);
    }

    @Bean
    GlobalExceptionAdvice globalExceptionAdvice(final ErrorInfoExtractor errorInfoExtractor) {
        return new GlobalExceptionAdvice(this.serverProperties.getError(), errorInfoExtractor);
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
                        final DefinedErrorInfo errorStatus =
                                new DefinedErrorInfo(value.substring(i + 1),
                                        Integer.parseInt(value.substring(0,
                                                i)));
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
