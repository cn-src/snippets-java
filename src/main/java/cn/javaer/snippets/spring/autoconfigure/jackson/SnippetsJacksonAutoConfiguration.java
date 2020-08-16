package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import cn.javaer.snippets.jackson.Json;
import cn.javaer.snippets.jackson.SnippetsJacksonIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.jooq.JSONB;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ObjectMapper.class})
@AutoConfigureBefore(JacksonAutoConfiguration.class)
@ConditionalOnProperty(prefix = "snippets.jackson", name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties(SnippetsJacksonProperties.class)
public class SnippetsJacksonAutoConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer snippetsJacksonCustomizer(final SnippetsJacksonProperties snippetsJacksonProperties) {
        return it -> {
            it.annotationIntrospector(SnippetsJacksonIntrospector.INSTANCE);

            final DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern(snippetsJacksonProperties.getFormat().getDateTime());
            final DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(snippetsJacksonProperties.getFormat().getDate());
            final DateTimeFormatter timeFormatter =
                    DateTimeFormatter.ofPattern(snippetsJacksonProperties.getFormat().getTime());

            it.deserializerByType(LocalDateTime.class,
                    new LocalDateTimeDeserializer(dateTimeFormatter));
            it.deserializerByType(LocalDate.class,
                    new LocalDateDeserializer(dateFormatter));
            it.deserializerByType(LocalTime.class,
                    new LocalTimeDeserializer(timeFormatter));

            it.serializerByType(LocalDateTime.class,
                    new LocalDateTimeSerializer(dateTimeFormatter));
            it.serializerByType(LocalDate.class,
                    new LocalDateSerializer(dateFormatter));
            it.serializerByType(LocalTime.class,
                    new LocalTimeSerializer(timeFormatter));
        };
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(Json.class)
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public Json json(final ObjectMapper objectMapper) {
        return new Json(objectMapper);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({JSONB.class})
    public static class JooqJacksonAutoConfiguration {
        @Bean
        public Jackson2ObjectMapperBuilderCustomizer snippetsJooqJacksonCustomizer() {
            return it -> it.serializerByType(JSONB.class, JooqJsonbSerializer.INSTANCE);
        }
    }
}
