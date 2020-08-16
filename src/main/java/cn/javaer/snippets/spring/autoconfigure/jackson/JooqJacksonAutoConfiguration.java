package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jooq.JSONB;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JSONB.class})
@ConditionalOnBean({ObjectMapper.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "snippets.jackson", name = "enabled", havingValue = "true",
        matchIfMissing = true)
public class JooqJacksonAutoConfiguration implements InitializingBean {
    private final ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JooqJacksonAutoConfiguration(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() {
        if (null != this.objectMapper) {
            final SimpleModule module = new SimpleModule();
            module.addSerializer(JooqJsonbSerializer.INSTANCE);

            this.objectMapper.registerModule(module);
        }
    }
}
