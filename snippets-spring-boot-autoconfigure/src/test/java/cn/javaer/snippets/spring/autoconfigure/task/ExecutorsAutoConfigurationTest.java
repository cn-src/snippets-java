package cn.javaer.snippets.spring.autoconfigure.task;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class ExecutorsAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(
            ExecutorsAutoConfiguration.class))
        .withPropertyValues(
            "snippets.execution.executors.demoExecutor1.threadNamePrefix=demo1-",
            "snippets.execution.executors.demoExecutor2.threadNamePrefix=demo2-"
        );

    @Test
    void postProcessBeanFactory() {

        this.contextRunner.run(context -> {
            assertThat(context).hasBean("demoExecutor1");
            assertThat(context).hasBean("demoExecutor2");

            final ThreadPoolTaskExecutor demoExecutor1 = context.getBean("demoExecutor1",
                ThreadPoolTaskExecutor.class);
            assertThat(demoExecutor1.getThreadNamePrefix()).isEqualTo("demo1-");

            final ThreadPoolTaskExecutor demoExecutor2 = context.getBean("demoExecutor2",
                ThreadPoolTaskExecutor.class);
            assertThat(demoExecutor2.getThreadNamePrefix()).isEqualTo("demo2-");
        });
    }
}