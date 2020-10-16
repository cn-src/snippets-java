package cn.javaer.snippets.spring.autoconfigure.data.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Pageable.class)
@AutoConfigureBefore(SpringDataWebAutoConfiguration.class)
@ConditionalOnProperty(prefix = "snippets.spring.data.pageable", name = "enabled", havingValue =
    "true",
    matchIfMissing = true)
@EnableConfigurationProperties(SpringDataWebProperties.class)
public class PageableAutoConfiguration implements InitializingBean {
    private static final String PAGE_PARAMETER = "spring.data.web.pageable.page-parameter";
    private static final String SIZE_PARAMETER = "spring.data.web.pageable.size-parameter";
    private static final String ONE_INDEXED_PARAMETERS = "spring.data.web.pageable" +
        ".one-indexed-parameters";
    private final SpringDataWebProperties properties;
    private final Environment environment;

    public PageableAutoConfiguration(final SpringDataWebProperties properties,
                                     final Environment environment) {
        this.properties = properties;
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() {
        final SpringDataWebProperties.Pageable pageable = this.properties.getPageable();
        if (!this.environment.containsProperty(PAGE_PARAMETER)) {
            pageable.setPageParameter("_page");
        }
        if (!this.environment.containsProperty(SIZE_PARAMETER)) {
            pageable.setSizeParameter("_size");
        }
        if (!this.environment.containsProperty(ONE_INDEXED_PARAMETERS)) {
            pageable.setOneIndexedParameters(true);
        }
    }
}
