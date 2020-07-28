package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jooq.JSONB;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ObjectMapper.class, JSONB.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class JooqJacksonAutoConfiguration implements InitializingBean {
    private final ObjectMapper objectMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JooqJacksonAutoConfiguration(@Autowired(required = false) final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null != this.objectMapper) {
            final SimpleModule module = new SimpleModule();
            module.addSerializer(JooqJsonbSerializer.INSTANCE);
            this.objectMapper.registerModule(module);
        }
    }
}
