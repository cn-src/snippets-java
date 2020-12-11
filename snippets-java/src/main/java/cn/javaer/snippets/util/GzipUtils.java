package cn.javaer.snippets.util;

import org.springframework.lang.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author cn-src
 */
public interface GzipUtils {

    /**
     * gzip 压缩, UFT-8 编码.
     *
     * @param str string
     *
     * @return byte[]
     */
    @Nullable
    static byte[] compress(final String str) {
        return compress(str, StandardCharsets.UTF_8);
    }

    /**
     * GZIP 压缩.
     *
     * @param str string
     * @param encoding 编码
     *
     * @return byte[]
     */
    @Nullable
    static byte[] compress(final String str, final Charset encoding) {
        return compress(str.getBytes(encoding));
    }

    /**
     * GZIP 压缩.
     *
     * @param bytes bytes
     *
     * @return byte[]
     */
    @Nullable
    static byte[] compress(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final GZIPOutputStream gzip = new GZIPOutputStream(out)) {

            gzip.write(bytes);
            gzip.finish();
            // toByteArray 前需刷新缓存
            return out.toByteArray();
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * GZIP 解压缩.
     *
     * @param bytes data
     *
     * @return data
     */
    @Nullable
    static byte[] unCompress(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             final GZIPInputStream unGzip = new GZIPInputStream(in)) {

            final byte[] buffer = new byte[1024 * 4];
            int n;
            while ((n = unGzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            out.flush();
            return out.toByteArray();
        }
        catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * GZIP 解压缩.
     *
     * @param bytes data
     *
     * @return data
     */
    @Nullable
    static String unCompressToString(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return new String(unCompress(bytes), StandardCharsets.UTF_8);
    }
}
