package cn.javaer.snippets.spring.autoconfigure.web.exception;

import cn.javaer.snippets.spring.exception.DefinedErrorInfo;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.View;

import java.util.HashMap;
import java.util.Map;

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
    ErrorInfoExtractor errorInfoExtractor() {
        return new ErrorInfoExtractor(this.useMapping);
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