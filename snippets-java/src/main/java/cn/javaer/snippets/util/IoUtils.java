package cn.javaer.snippets.util;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;

/**
 * @author cn-src
 */
public interface IoUtils {

    /**
     * 读取文件为字符串.
     *
     * @param file File
     *
     * @return String
     *
     * @see #readToString(Path, Charset) #readToString(Path, Charset)
     */
    static String readToString(final File file) {
        return readToString(file, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件为字符串.
     *
     * @param file File
     * @param charset Charset
     *
     * @return String
     *
     * @see #readToString(Path, Charset) #readToString(Path, Charset)
     */
    static String readToString(final File file, final Charset charset) {
        return readToString(file.toPath(), charset);
    }

    /**
     * 读取文件为字符串.
     *
     * @param file Path
     *
     * @return String
     *
     * @see #readToString(Path, Charset) #readToString(Path, Charset)
     */
    static String readToString(final Path file) {
        return readToString(file, StandardCharsets.UTF_8);
    }

    /**
     * 将文件读取为 String.
     *
     * @param file 文件
     * @param charset 文件编码
     *
     * @return String
     */
    static String readToString(final Path file, final Charset charset) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(charset);
        try {
            final byte[] bytes = Files.readAllBytes(file);
            return new String(bytes, charset);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 创建多级目录
     *
     * @param dir 目录
     */
    static void createDirectories(final Path dir) {
        try {
            Files.createDirectories(dir);
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 重新生成此目录.
     *
     * @param dir dir
     */
    static void recreateDirectories(final Path dir) {
        deleteRecursively(dir);
        createDirectories(dir);
    }

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
