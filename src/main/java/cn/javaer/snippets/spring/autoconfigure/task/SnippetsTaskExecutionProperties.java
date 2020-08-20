package cn.javaer.snippets.spring.autoconfigure.task;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.task.TaskDecorator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cn-src
 */
@ConfigurationProperties(prefix = "snippets.task")
public class SnippetsTaskExecutionProperties {
    @NestedConfigurationProperty
    private Map<String, TaskProperties> executions = new LinkedHashMap<>();

    public Map<String, TaskProperties> getExecutions() {
        return this.executions;
    }

    public void setExecutors(final Map<String, TaskProperties> executions) {
        this.executions = executions;
    }

    static class TaskProperties extends TaskExecutionProperties {
        private TaskDecorator taskDecorator;

        public TaskDecorator getTaskDecorator() {
            return this.taskDecorator;
        }

        public void setTaskDecorator(final TaskDecorator taskDecorator) {
            this.taskDecorator = taskDecorator;
        }
    }
}
