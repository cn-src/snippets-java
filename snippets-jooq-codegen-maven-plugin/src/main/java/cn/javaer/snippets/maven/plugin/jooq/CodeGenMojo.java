package cn.javaer.snippets.maven.plugin.jooq;

import cn.javaer.snippets.jooq.codegen.withentity.CodeGenConfig;
import cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

/**
 * @author cn-src
 */
@SuppressWarnings("unused")
@Mojo(
    name = "generate",
    defaultPhase = GENERATE_SOURCES,
    requiresDependencyResolution = TEST,
    threadSafe = true
)
public class CodeGenMojo extends AbstractMojo {

    @Parameter(
        property = "project",
        required = true,
        readonly = true
    )
    private MavenProject project;

    @Parameter(
        property = "jooq.codegen.skip"
    )
    private boolean skip;

    @Parameter(
        property = "jooq.codegen.basedir"
    )
    private String basedir;

    @Parameter(
        required = true,
        property = "jooq.codegen.packageName"
    )
    private String packageName;

    @Parameter(
        property = "jooq.codegen.includePackage"
    )
    private String includePackage;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(
        property = "jooq.codegen.includePackages"
    )
    private List<String> includePackages;

    @Parameter(
        property = "jooq.codegen.includeClass"
    )
    private String includeClass;

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(
        property = "jooq.codegen.includeClasses"
    )
    private List<String> includeClasses;

    @Override
    public void execute() throws MojoExecutionException {
        if (this.skip) {
            this.getLog().info("Skipping jOOQ code generation");
            return;
        }
        final String actualBasedir = this.basedir == null ?
            Paths.get(this.project.getBasedir().getAbsolutePath(), "src/main/java").toString() :
            this.basedir;

        final CodeGenConfig config = CodeGenConfig.builder()
            .classLoader(this.getClassLoader())
            .generatedSrcPackage(this.packageName)
            .generatedSrcRootDir(actualBasedir)
            .scanClassName(this.includeClass)
            .scanPackageName(this.includePackage)
            .scanPackageNames(this.includePackages)
            .scanClassNames(this.includeClasses)
            .build();
        if (config.isNotScan()) {
            return;
        }
        try {
            CodeGenTool.generate(config);
        }
        catch (final Exception ex) {
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        }
    }

    private URLClassLoader getClassLoader() throws MojoExecutionException {
        try {
            final List<String> classpathElements = this.project.getRuntimeClasspathElements();
            final URL[] urls = new URL[classpathElements.size()];

            for (int i = 0; i < urls.length; i++) {
                urls[i] = new File(classpathElements.get(i)).toURI().toURL();
            }

            return new URLClassLoader(urls, this.getClass().getClassLoader());
        }
        catch (final Exception e) {
            throw new MojoExecutionException("Couldn't create a classloader.", e);
        }
    }
}
