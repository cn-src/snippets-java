package cn.javaer.snippets.spring.autoconfigure.p6spy;

import cn.javaer.snippets.p6spy.BeautifulFormat;
import cn.javaer.snippets.p6spy.TimestampJdbcEventListener;
import com.github.gavlyukovskiy.boot.jdbc.decorator.DataSourceDecoratorAutoConfiguration;
import com.p6spy.engine.common.Loggable;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc 支持.
 *
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Loggable.class, BeautifulFormat.class, TimestampJdbcEventListener.class})
@AutoConfigureBefore({DataSourceDecoratorAutoConfiguration.class})
@ConditionalOnProperty(prefix = "snippets.p6spy", name = "enabled", havingValue = "true",
    matchIfMissing = true)
public class P6spyAutoConfiguration implements InitializingBean {

    @Bean
    @ConditionalOnMissingBean(TimestampJdbcEventListener.class)
    public TimestampJdbcEventListener timestampJdbcEventListener() {
        return new TimestampJdbcEventListener();
    }

    @Override
    public void afterPropertiesSet() {
        System.setProperty("p6spy.config.logMessageFormat",
            BeautifulFormat.class.getName());
        System.setProperty("p6spy.config.customLogMessageFormat",
            "time %(executionTime) ms\n%(sql)");
    }
}
