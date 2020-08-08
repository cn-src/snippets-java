package cn.javaer.snippets.spring.exception;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author cn-src
 */
@RestController
@RequestMapping
public class ErrorInfoController implements ApplicationContextAware {
    private final Map<String, Info> errorInfos = new HashMap<>();
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() throws IOException {
        final Properties props = new Properties();
        try (final InputStream input = this.getClass().getResourceAsStream("/default-errors" +
                "-messages.properties")) {
            final StringReader reader = new StringReader(StreamUtils.copyToString(input,
                    StandardCharsets.UTF_8));
            props.load(reader);
            @SuppressWarnings({"unchecked", "rawtypes"})
            final Set<String> keySet = (Set) props.keySet();
            keySet.forEach(it -> this.errorInfos.put(it, new Info()));

            final Collection<Object> controllers =
                    this.applicationContext.getBeansWithAnnotation(Controller.class).values();
            for (final Object ctr : controllers) {
                final Method[] methods = ctr.getClass().getDeclaredMethods();
                for (final Method method : methods) {
                    if (null != AnnotationUtils.findAnnotation(method, RequestMapping.class)) {
                        final Class<?>[] exceptionTypes = method.getExceptionTypes();
                        for (final Class<?> clazz : exceptionTypes) {
                            final ResponseStatus responseStatus =
                                    AnnotationUtils.findAnnotation(clazz, ResponseStatus.class);
                            if (null != responseStatus) {
                                if (keySet.contains(responseStatus.reason())) {
                                    this.errorInfos.get(responseStatus.reason()).status =
                                            responseStatus.code().value();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @GetMapping("error_infos")
    public String get() {
        return "";
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    static class Info {
        Integer status;
        String message;
    }
}
