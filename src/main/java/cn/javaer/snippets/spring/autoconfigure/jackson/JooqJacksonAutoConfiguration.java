package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import org.jooq.JSONB;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.context.annotation.Configuration;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JSONB.class})
@ConditionalOnBean({JsonComponentModule.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "snippets.jackson", name = "enabled", havingValue = "true",
        matchIfMissing = true)
public class JooqJacksonAutoConfiguration implements InitializingBean {
    private final JsonComponentModule jsonComponentModule;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JooqJacksonAutoConfiguration(final JsonComponentModule jsonComponentModule) {
        this.jsonComponentModule = jsonComponentModule;
    }

    @Override
    public void afterPropertiesSet() {
        this.jsonComponentModule.addSerializer(JooqJsonbSerializer.INSTANCE);
    }
}
