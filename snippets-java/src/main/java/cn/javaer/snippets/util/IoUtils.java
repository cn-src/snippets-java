package cn.javaer.snippets.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

/**
 * @author cn-src
 */
public interface IoUtils {

    /**
     * 删除目录以及所有文件
     *
     * @param root 根路径
     *
     * @return 是否删除
     */
    static boolean deleteRecursively(final File root) {
        return deleteRecursively(root.toPath());
    }

    /**
     * 删除目录以及所有文件
     *
     * @param root 根路径
     *
     * @return 是否删除
     */
    static boolean deleteRecursively(final Path root) {
        if (root == null) {
            return false;
        }
        if (!Files.exists(root)) {
            return false;
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            Files.walk(root)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
        return true;
    }
}
