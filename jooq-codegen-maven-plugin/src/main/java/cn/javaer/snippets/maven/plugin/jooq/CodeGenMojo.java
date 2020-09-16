package cn.javaer.snippets.maven.plugin.jooq;

import cn.javaer.snippets.jooq.codegen.withentity.CodeGenTool;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;
import java.util.List;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

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
        property = "jooq.codegen.basedir"
    )
    private String basedir;

    @Parameter(
        property = "jooq.codegen.packageName"
    )
    private String packageName;

    @Parameter(
        property = "jooq.codegen.includePackages"
    )
    private List<String> includePackages;

    @Override
    public void execute()
        throws MojoExecutionException {
        final String actualBasedir = this.basedir == null ?
            Paths.get(this.project.getBasedir().getAbsolutePath(), "src/main/java").toString() :
            this.basedir;
        CodeGenTool.generate(actualBasedir, this.packageName,
            this.includePackages.toArray(new String[0]));
    }
}
