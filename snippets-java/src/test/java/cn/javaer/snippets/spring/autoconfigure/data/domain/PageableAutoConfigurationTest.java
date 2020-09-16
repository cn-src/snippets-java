package cn.javaer.snippets.spring.autoconfigure.data.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author cn-src
 */
class PageableAutoConfigurationTest {
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(PageableAutoConfiguration.class,
                    SpringDataWebAutoConfiguration.class, MockMvcAutoConfiguration.class,
                    WebMvcAutoConfiguration.class, DispatcherServletAutoConfiguration.class,
                    HttpMessageConvertersAutoConfiguration.class));

    @Test
    void config() {
        this.contextRunner.withUserConfiguration(DemoController.class)
                .run(context -> {
                    final MockMvc mockMvc = context.getBean(MockMvc.class);
                    mockMvc.perform(get("/test?_page=9&_size=11")).andExpect(status().isOk());
                });
    }

    @RestController
    @RequestMapping
    static class DemoController {

        @GetMapping("test")
        public Page<String> get(final Pageable pageable) {
            // 启用 1 为起始页码，而 pageable 实际为 0 所以实际需要 -1
            assertThat(pageable.getPageNumber()).isEqualTo(8);
            assertThat(pageable.getPageSize()).isEqualTo(11);
            return new PageImpl<>(Collections.singletonList("page data"), pageable, 1);
        }
    }
}