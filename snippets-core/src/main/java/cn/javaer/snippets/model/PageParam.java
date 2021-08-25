package cn.javaer.snippets.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 分页参数.
 *
 * @author cn-src
 */
@Data
@ParameterObject
@Setter(AccessLevel.PACKAGE)
public class PageParam {

    PageParam() {
        this.page = 1;
        this.size = 20;
    }

    PageParam(final int page, final int size, final long offset) {
        this.page = Math.max(page, 1);
        this.size = Math.max(size, 1);
        this.offset = offset;
    }

    @Schema(name = "page", description = "分页-页码", minimum = "1", defaultValue = "1")
    int page;

    @Schema(name = "size", description = "分页-大小", minimum = "1", defaultValue = "20")
    int size;

    @Hidden
    long offset;

    public static PageParam of(final int page, final int size) {
        return new PageParam(page, size, (long) (page - 1) * (long) size);
    }
}