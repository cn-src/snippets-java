package cn.javaer.snippets.spring.autoconfigure.task;

import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author cn-src
 */
@ConfigurationProperties(prefix = "snippets.scheduling")
public class SchedulersProperties {
    @NestedConfigurationProperty
    private Map<String, TaskProperties> schedulers = new LinkedHashMap<>();

    public Map<String, TaskProperties> getSchedulers() {
        return this.schedulers;
    }

    public void setSchedulers(final Map<String, TaskProperties> schedulers) {
        this.schedulers = schedulers;
    }

    static class TaskProperties extends TaskSchedulingProperties {
    }
}
