package demo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
public class MyMojo
    extends AbstractMojo {
    /**
     * Location of the file.
     *
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    @Override
    public void execute()
        throws MojoExecutionException {
        final File f = this.outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        final File touch = new File(f, "touch.txt");

        FileWriter w = null;
        try {
            w = new FileWriter(touch);

            w.write("touch.txt");
        }
        catch (final IOException e) {
            throw new MojoExecutionException("Error creating file " + touch, e);
        }
        finally {
            if (w != null) {
                try {
                    w.close();
                }
                catch (final IOException e) {
                    // ignore
                }
            }
        }
    }
}
