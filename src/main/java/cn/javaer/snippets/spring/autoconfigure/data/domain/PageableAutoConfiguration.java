package cn.javaer.snippets.spring.autoconfigure.data.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
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
    private final SpringDataWebProperties properties;

    public PageableAutoConfiguration(final SpringDataWebProperties properties) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() {
        final SpringDataWebProperties.Pageable pageable = this.properties.getPageable();
        pageable.setPageParameter("_page");
        pageable.setSizeParameter("_size");
        pageable.setOneIndexedParameters(true);
    }
}
