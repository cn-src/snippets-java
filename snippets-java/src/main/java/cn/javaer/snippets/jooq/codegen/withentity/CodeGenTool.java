package cn.javaer.snippets.jooq.codegen.withentity;

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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author cn-src
 */
public class CodeGenTool {
    private static final Logger logger = Logger.getLogger(CodeGenTool.class.getName());
    static ClassInfoList enums = null;

    public static void generate(final String dir, final String genPackage,
                                final String... packageNamesToScan) {
        logger.info("Code generation with packages:" + Arrays.toString(packageNamesToScan));
        final TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setSuffix(".hbs");
        final Handlebars handlebars = new Handlebars(loader);
        final Path genDir = getFilePath(dir, genPackage);
        try {
            Files.deleteIfExists(genDir);
            Files.createDirectories(genDir);
            final Template template = handlebars.compile("jooq-gen-table");
            final List<TableMeta> tableMetas = scan(genPackage, packageNamesToScan);
            logger.info("Scan entities total:" + tableMetas.size());
            for (final TableMeta tableMeta : tableMetas) {
                logger.info("Generate:" + tableMeta.getEntityName());
                try (final FileWriter writer = new FileWriter(
                    Paths.get(genDir.toString(), tableMeta.getTableClassName() + ".java").toFile())) {

                    template.apply(tableMeta, writer);
                }
            }
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static Path getFilePath(final String dir, final String genPackage) {
        final String packageToPath = genPackage.replaceAll("\\.", "/");
        return Paths.get(dir, packageToPath);
    }

    static List<TableMeta> scan(final String genPackage, final String... packageNames) {

        try (final ScanResult scanResult = new ClassGraph()
            .addClassLoader(Thread.currentThread().getContextClassLoader())
            .enableAllInfo()
            .acceptPackages(packageNames)
            .scan()) {
            final ClassInfoList classInfoList =
                scanResult.getClassesWithFieldAnnotation("org.springframework.data.annotation.Id");

            enums = scanResult.getAllEnums();

            return classInfoList.stream().map(it -> new TableMeta(it, genPackage))
                .collect(Collectors.toList());
        }
    }
}
