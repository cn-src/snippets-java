package cn.javaer.snippets.jooq.codegen.withentity;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * @author cn-src
 */
@Data
@Builder
public class CodeGenConfig {
    private ClassLoader classLoader;
    private String generatedSrcRootDir;
    private String generatedSrcPackage;
    @Singular
    private Set<String> scanPackageNames;
    @Singular
    private Set<String> scanClassNames;

    public Path generatedDir() {
        final String packageToPath = this.generatedSrcPackage.replaceAll("\\.", "/");
        return Paths.get(this.generatedSrcRootDir, packageToPath);
    }

    public ClassLoader classLoader() {
        return this.classLoader == null ?
            Thread.currentThread().getContextClassLoader() : this.classLoader;
    }

    public String[] scanPackageNames() {
        return this.scanPackageNames == null ?
            new String[0] : this.scanPackageNames.toArray(new String[0]);
    }

    public String[] scanClassNames() {
        return this.scanClassNames == null ?
            new String[0] : this.scanClassNames.toArray(new String[0]);
    }

    public boolean isNotScan() {
        return (this.scanClassNames == null || this.scanClassNames.isEmpty())
            && (this.scanPackageNames == null || this.scanPackageNames.isEmpty());
    }
}
