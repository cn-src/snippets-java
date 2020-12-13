package cn.javaer.snippets.maven.plugin.jooq;

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

    @Override
    public void execute() throws MojoExecutionException {
        if (this.skip) {
            this.getLog().info("Skipping jOOQ code generation");
            return;
        }
        final ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        final URLClassLoader pluginClassLoader = this.getClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginClassLoader);
            final String actualBasedir = this.basedir == null ?
                Paths.get(this.project.getBasedir().getAbsolutePath(), "src/main/java").toString() :
                this.basedir;
            if (null != this.includePackages && !this.includePackages.isEmpty()) {
                CodeGenTool.generate(actualBasedir, this.packageName,
                    this.includePackages.toArray(new String[0]));
            }
            else {
                CodeGenTool.generate(actualBasedir, this.packageName, this.includePackage);
            }
        }
        catch (final Exception ex) {
            throw new MojoExecutionException("Error running jOOQ code generation tool", ex);
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
            try {
                pluginClassLoader.close();
            }
            catch (final Throwable e) {
                this.getLog().error("Couldn't close the classloader.", e);
            }
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
