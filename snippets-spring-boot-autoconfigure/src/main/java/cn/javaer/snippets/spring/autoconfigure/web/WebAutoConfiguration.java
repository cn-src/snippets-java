package cn.javaer.snippets.spring.autoconfigure.web;

import cn.javaer.snippets.spring.format.DateTimeFormatter;
import cn.javaer.snippets.spring.web.DefaultAppContext;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cn-src
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@ConditionalOnProperty(prefix = "snippets.web", name = "enabled", havingValue = "true",
    matchIfMissing = true)
public class WebAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class SnippetsWebMvcConfigurer implements WebMvcConfigurer {
        @Override
        public void addFormatters(final FormatterRegistry registry) {
            registry.addFormatterForFieldAnnotation(new DateTimeFormatter());
        }

        @Override
        public void addInterceptors(final InterceptorRegistry registry) {
            registry.addInterceptor(new HandlerInterceptor() {
                @Override
                public boolean preHandle(final @NotNull HttpServletRequest request,
                                         final @NotNull HttpServletResponse response,
                                         final @NotNull Object handler) {
                    DefaultAppContext.setRequestId();
                    response.addHeader(DefaultAppContext.REQUEST_ID_PARAM,
                        DefaultAppContext.getRequestId());
                    return true;
                }
            });
        }
    }
}
