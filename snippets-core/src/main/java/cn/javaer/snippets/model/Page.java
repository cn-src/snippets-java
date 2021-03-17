package cn.javaer.snippets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * @author cn-src
 */
@Data
@Setter(AccessLevel.PACKAGE)
public class Page<T> {
    Page(final List<T> content, final long total) {
        this.content = content;
        this.total = total;
    }

    @Schema(description = "分页-内容")
    private List<T> content;

    @Schema(description = "分页-总数")
    private long total;

    public static <T> Page<T> of(final List<T> content, final long total) {
        return new Page<>(content, total);
    }
}
