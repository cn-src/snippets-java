package cn.javaer.snippets.jooq.codegen.withentity;

import org.junit.jupiter.api.Test;

/**
 * @author cn-src
 */
class CodeGenToolTest {

    @Test
    void generate() {
        final String dir = CodeGenToolTest.class.getResource("/").getPath();
        final CodeGenConfig config = CodeGenConfig.builder()
            .generatedSrcRootDir(dir)
            .generatedSrcPackage("demo")
            .scanPackageName("cn.javaer.snippets.jooq.codegen.withentity")
            .build();
        CodeGenTool.generate(config);
    }
}