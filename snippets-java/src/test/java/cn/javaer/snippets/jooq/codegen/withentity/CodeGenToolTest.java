package cn.javaer.snippets.jooq.codegen.withentity;

import org.junit.jupiter.api.Test;

/**
 * @author cn-src
 */
class CodeGenToolTest {

    @Test
    void generate() {
        final String dir = CodeGenToolTest.class.getResource("/").getPath();
        CodeGenTool.generate(dir, "demo", "cn.javaer.snippets.jooq.codegen.withentity");
    }
}