package cn.javaer.snippets.spring.autoconfigure.task;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cn-src
 */
@ConfigurationProperties(prefix = "snippets.task")
public class SnippetsTaskExecutionProperties {
    @NestedConfigurationProperty
    private Map<String, TaskProperties> execution = new LinkedHashMap<>();

    public Map<String, TaskProperties> getExecutions() {
        return this.execution;
    }

    public void setExecution(final Map<String, TaskProperties> executions) {
        this.execution = executions;
    }

    static class TaskProperties extends TaskExecutionProperties {
    }
}
