package cn.javaer.snippets.spring.web;

import cn.javaer.snippets.spring.format.DateTimeFormatter;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.FormatterRegistry;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author cn-src
 */
public class SnippetsWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(new DateTimeFormatter());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(new WebRequestInterceptor() {
            @Override
            public void preHandle(final @NotNull WebRequest request) {
                DefaultAppContext.setRequestId();
            }

            @Override
            public void postHandle(final @NotNull WebRequest request, final ModelMap model) {
            }

            @Override
            public void afterCompletion(final @NotNull WebRequest request, final Exception ex) {
            }
        });
    }
}
