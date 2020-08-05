package cn.javaer.snippets.spring.autoconfigure.jackson;

import cn.javaer.snippets.jackson.JooqJsonbSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.jooq.JSONB;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ObjectMapper.class, JSONB.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
@EnableConfigurationProperties(SnippetsJacksonProperties.class)
public class JooqJacksonAutoConfiguration implements InitializingBean {
    private final ObjectMapper objectMapper;
    private final SnippetsJacksonProperties snippetsJacksonProperties;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JooqJacksonAutoConfiguration(@Autowired(required = false) final ObjectMapper objectMapper,
                                        final SnippetsJacksonProperties snippetsJacksonProperties) {
        this.objectMapper = objectMapper;
        this.snippetsJacksonProperties = snippetsJacksonProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (null != this.objectMapper) {
            final SimpleModule module = new SimpleModule();
            module.addSerializer(JooqJsonbSerializer.INSTANCE);

            final DateTimeFormatter dateTimeFormatter =
                    DateTimeFormatter.ofPattern(this.snippetsJacksonProperties.getFormat().getDateTime());
            final DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(this.snippetsJacksonProperties.getFormat().getDate());
            final DateTimeFormatter timeFormatter =
                    DateTimeFormatter.ofPattern(this.snippetsJacksonProperties.getFormat().getTime());

            module.addDeserializer(LocalDateTime.class,
                    new LocalDateTimeDeserializer(dateTimeFormatter));
            module.addDeserializer(LocalDate.class,
                    new LocalDateDeserializer(dateFormatter));
            module.addDeserializer(LocalTime.class,
                    new LocalTimeDeserializer(timeFormatter));

            module.addSerializer(LocalDateTime.class,
                    new LocalDateTimeSerializer(dateTimeFormatter));
            module.addSerializer(LocalDate.class,
                    new LocalDateSerializer(dateFormatter));
            module.addSerializer(LocalTime.class,
                    new LocalTimeSerializer(timeFormatter));

            this.objectMapper.registerModule(module);
        }
    }
}
