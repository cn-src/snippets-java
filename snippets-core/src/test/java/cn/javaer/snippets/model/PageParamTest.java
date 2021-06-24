package cn.javaer.snippets.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class PageParamTest {

    @Test
    void name() {
        final PageParam pageParam = PageParam.of(1, 20);
        assertThat(pageParam.getOffset()).isEqualTo(0L);
    }
}