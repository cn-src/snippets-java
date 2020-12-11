package cn.javaer.snippets.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class GzipUtilsTest {
    @Test
    void zip() {
        final byte[] bytes = GzipUtils.compress("demo");
        final String str = GzipUtils.unCompressToString(bytes);
        assertThat(str).isEqualTo("demo");
    }
}