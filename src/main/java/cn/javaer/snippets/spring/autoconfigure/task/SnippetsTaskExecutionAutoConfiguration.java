package cn.javaer.snippets.spring.autoconfigure.task;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author cn-src
 */
@ConditionalOnClass(ThreadPoolTaskExecutor.class)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SnippetsTaskExecutionProperties.class)
public class SnippetsTaskExecutionAutoConfiguration implements ApplicationContextAware {

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        final SnippetsTaskExecutionProperties properties =
                applicationContext.getBean(SnippetsTaskExecutionProperties.class);
        final DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) applicationContext
                        .getAutowireCapableBeanFactory();

        if (!CollectionUtils.isEmpty(properties.getExecutions())) {

            for (final Map.Entry<String, SnippetsTaskExecutionProperties.TaskProperties> entry :
                    properties.getExecutions().entrySet()) {
                final SnippetsTaskExecutionProperties.TaskProperties taskProp =
                        entry.getValue();
                final TaskExecutionProperties.Pool pool = taskProp.getPool();
                TaskExecutorBuilder builder = new TaskExecutorBuilder();
                builder = builder.queueCapacity(pool.getQueueCapacity());
                builder = builder.corePoolSize(pool.getCoreSize());
                builder = builder.maxPoolSize(pool.getMaxSize());
                builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
                builder = builder.keepAlive(pool.getKeepAlive());
                final TaskExecutionProperties.Shutdown shutdown = taskProp.getShutdown();
                builder = builder.awaitTermination(shutdown.isAwaitTermination());
                builder =
                        builder.awaitTerminationPeriod(shutdown
                                .getAwaitTerminationPeriod());
                builder = builder.threadNamePrefix(taskProp.getThreadNamePrefix());

                builder = builder.taskDecorator(taskProp.getTaskDecorator());
                beanFactory.registerSingleton(entry.getKey(), builder.build());
            }
        }
    }
}
