package cn.javaer.snippets.jooq.codegen.withentity;

import cn.javaer.snippets.util.IoUtils;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * jOOQ 代码生成工具，针对注解了 {@link org.springframework.data.relational.core.mapping.Table}
 * 或者 {@link org.springframework.data.annotation.Id} 的实体类的元数据信息生成类型安全的 jOOQ 代码。
 *
 * @author cn-src
 */
@SuppressWarnings("JavadocReference")
public class CodeGenTool {
    private static final Logger logger = Logger.getLogger(CodeGenTool.class.getName());
    static ClassInfoList enums = null;

    /**
     * 生成代码.
     *
     * @param dir 生成代码存放的根目录
     * @param genPackage 生成代码的 package
     * @param packageNamesToScan 要扫面的实体类或者相关的枚举类等的 package
     */
    public static void generate(final CodeGenConfig config) {
        logger.info("Code generation with packages:" + config);
        final TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setSuffix(".hbs");
        final Handlebars handlebars = new Handlebars(loader);
        final Path generatedDir = config.generatedDir();
        try {
            IoUtils.recreateDirectories(generatedDir);
            final Template template = handlebars.compile("jooq-gen-table");
            final List<TableMeta> tableMetas = scan(config);
            logger.info("Scan entities total:" + tableMetas.size());
            for (final TableMeta tableMeta : tableMetas) {
                logger.info("Generate:" + tableMeta.getEntityName());
                try (final FileWriter writer = new FileWriter(
                    Paths.get(generatedDir.toString(), tableMeta.getTableClassName() + ".java").toFile())) {
                    template.apply(tableMeta, writer);
                }
            }
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static List<TableMeta> scan(final CodeGenConfig config) {
        try (final ScanResult scanResult = new ClassGraph()
            .addClassLoader(config.classLoader())
            .enableAllInfo()
            .acceptPackages(config.scanPackageNames())
            .acceptClasses(config.scanClassNames())
            .scan()) {
            final ClassInfoList tableClass = scanResult.getClassesWithAnnotation(
                "org.springframework.data.relational.core.mapping.Table");
            final ClassInfoList idClass =
                scanResult.getClassesWithFieldAnnotation("org.springframework.data.annotation.Id");
            enums = scanResult.getAllEnums();
            final ClassInfoList tableMetas = new ClassInfoList();
            tableMetas.addAll(tableClass);
            tableMetas.addAll(idClass);
            return tableMetas.stream().distinct()
                .map(it -> new TableMeta(it, config.getGeneratedSrcPackage()))
                .collect(Collectors.toList());
        }
    }
}