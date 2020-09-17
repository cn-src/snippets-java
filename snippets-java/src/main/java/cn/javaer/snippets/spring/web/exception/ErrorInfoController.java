package cn.javaer.snippets.spring.web.exception;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author cn-src
 */
@RestController
@RequestMapping
public class ErrorInfoController implements ApplicationContextAware, InitializingBean {

    private Map<String, DefinedErrorInfo> errorInfos;
    private ApplicationContext applicationContext;
    private final ErrorInfoExtractor errorInfoExtractor;

    public ErrorInfoController(final ErrorInfoExtractor errorInfoExtractor) {
        this.errorInfoExtractor = errorInfoExtractor;
    }

    @GetMapping("error_infos")
    public Map<String, DefinedErrorInfo> errorInfos() {
        return this.errorInfos;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final Collection<Object> controllers =
            this.applicationContext.getBeansWithAnnotation(Controller.class).values();

        final Map<String, DefinedErrorInfo> infoMap = new HashMap<>();
        if (!this.errorInfoExtractor.getConfiguredErrorInfos().isEmpty()) {
            infoMap.putAll(this.errorInfoExtractor.getConfiguredErrorInfos());
        }

        final Map<String, DefinedErrorInfo> controllersErrorMapping =
            this.errorInfoExtractor.getControllersErrorMapping(controllers);
        if (!controllersErrorMapping.isEmpty()) {
            infoMap.putAll(controllersErrorMapping);
        }

        final TreeSet<Map.Entry<String, DefinedErrorInfo>> objects =
            new TreeSet<>(Comparator.comparing(it -> it.getValue().getStatus()));
        objects.addAll(infoMap.entrySet());

        this.errorInfos = new LinkedHashMap<>(objects.size());
        for (final Map.Entry<String, DefinedErrorInfo> entry : objects) {
            this.errorInfos.put(entry.getKey(), entry.getValue());
        }
    }
}
