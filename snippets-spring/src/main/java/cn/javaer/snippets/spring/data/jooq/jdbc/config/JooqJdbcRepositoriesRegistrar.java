package cn.javaer.snippets.spring.data.jooq.jdbc.config;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;

/**
 * @author cn-src
 */
class JooqJdbcRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
    @NonNull
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableJooqJdbcRepositories.class;
    }

    @NonNull
    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new JooqJdbcRepositoryConfigExtension();
    }
}
