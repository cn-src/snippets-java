package cn.javaer.snippets.spring.autoconfigure.springdoc;

import cn.javaer.snippets.spring.security.PrincipalId;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

/**
 * SpringDoc 支持.
 *
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({org.springframework.data.domain.Pageable.class})
@AutoConfigureAfter(name = {"org.springdoc.data.rest.SpringDocDataRestConfiguration"})
public class SpringDocPlusConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class,
            PageableDoc.class);
        SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.PageRequest.class,
            PageableDoc.class);
        SpringDocUtils.getConfig().replaceWithClass(Page.class, PageDoc.class);
        SpringDocUtils.getConfig().addAnnotationsToIgnore(PrincipalId.class);
    }
}