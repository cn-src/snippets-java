package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import org.jooq.JSONB;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JSONB.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "snippets.jackson", name = "enabled", havingValue = "true",
        matchIfMissing = true)
public class JooqJacksonAutoConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer snippetsJacksonCustomizer() {
        return it -> it.serializerByType(JSONB.class, JooqJsonbSerializer.INSTANCE);
    }
}
