package cn.javaer.snippets.spring.autoconfigure;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * @author cn-src
 */
public class SpringBoot {
    public static ConfigurableApplicationContext run(final Class<?> primarySource,
                                                     final String... args) {
        final SpringApplication app = new SpringApplication(primarySource);
        final Properties props = new Properties();
        try (final InputStream in =
                 SpringBoot.class.getResourceAsStream("/default-boot.properties")) {
            props.load(in);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        app.setDefaultProperties(props);
        return app.run(args);
    }
}
