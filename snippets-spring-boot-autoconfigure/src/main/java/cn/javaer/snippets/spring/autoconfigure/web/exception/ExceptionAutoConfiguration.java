package cn.javaer.snippets.spring.autoconfigure.web.exception;

import cn.javaer.snippets.exception.DefinedErrorInfo;
import cn.javaer.snippets.spring.web.exception.ErrorInfoController;
import cn.javaer.snippets.spring.web.exception.ErrorInfoExtractor;
import cn.javaer.snippets.spring.web.exception.GlobalErrorAttributes;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ExceptionMappingProperties.class, ServerProperties.class})
@ConditionalOnWebApplication
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
@ConditionalOnProperty(prefix = "snippets.web.exception", name = "enabled", havingValue = "true",
    matchIfMissing = true)
public class ExceptionAutoConfiguration implements InitializingBean {
    private final ExceptionMappingProperties exceptionMappingProperties;
    private Map<String, DefinedErrorInfo> useMapping;

    public ExceptionAutoConfiguration(
        final ExceptionMappingProperties exceptionMappingProperties,
        final ServerProperties serverProperties) {
        this.exceptionMappingProperties = exceptionMappingProperties;
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
        return new ErrorInfoExtractor(this.useMapping, messageSource);
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    GlobalErrorAttributes globalErrorAttributes(final ErrorInfoExtractor errorInfoExtractor) {
        return new GlobalErrorAttributes(errorInfoExtractor);
    }

    @Bean(name = "error")
    @ConditionalOnMissingBean(name = "error")
    View errorView() {
        return new ErrorView();
    }

    @Override
    public void afterPropertiesSet() {
        final Map<String, ExceptionMappingProperties.Error> mapping =
            this.exceptionMappingProperties.getMapping();

        if (!CollectionUtils.isEmpty(mapping)) {
            this.useMapping = new HashMap<>(mapping.size());
            for (final Map.Entry<String, ExceptionMappingProperties.Error> entry :
                mapping.entrySet()) {
                this.useMapping.put(entry.getKey(),
                    DefinedErrorInfo.of(entry.getValue().getError(), entry.getValue().getStatus()));
            }
        }
    }
}