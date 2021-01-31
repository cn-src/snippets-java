package cn.javaer.snippets.spring.autoconfigure.springdoc;

import cn.javaer.snippets.spring.autoconfigure.web.exception.ExceptionAutoConfiguration;
import cn.javaer.snippets.spring.security.PrincipalId;
import cn.javaer.snippets.spring.web.exception.ErrorInfoExtractor;
import cn.javaer.snippets.springdoc.PageDoc;
import cn.javaer.snippets.springdoc.PageableDoc;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OperationService;
import org.springdoc.core.PropertyResolverUtils;
import org.springdoc.core.ReturnTypeParser;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocConfiguration;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * SpringDoc 支持.
 *
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({org.springframework.data.domain.Pageable.class, SpringDocConfiguration.class})
@AutoConfigureAfter({ExceptionAutoConfiguration.class})
@AutoConfigureBefore({SpringDocConfiguration.class, SpringDocConfigProperties.class})
@ConditionalOnProperty(prefix = "snippets.springdoc", name = "enabled", havingValue = "true",
    matchIfMissing = true)
public class SpringDocAutoConfiguration implements InitializingBean {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @ConditionalOnMissingBean
    GenericResponseService responseBuilder(final OperationService operationBuilder,
                                           final ErrorInfoExtractor errorInfoExtractor,
                                           final List<ReturnTypeParser> returnTypeParsers,
                                           final SpringDocConfigProperties springDocConfigProperties,
                                           final PropertyResolverUtils propertyResolverUtils) {
        return new ExceptionResponseBuilder(operationBuilder, returnTypeParsers,
            springDocConfigProperties, propertyResolverUtils, errorInfoExtractor);
    }

    @Override
    public void afterPropertiesSet() {
        SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class,
            PageableDoc.class);
        SpringDocUtils.getConfig().replaceWithClass(Page.class, PageDoc.class);

        SpringDocUtils.getConfig().addAnnotationsToIgnore(PrincipalId.class);
    }
}
