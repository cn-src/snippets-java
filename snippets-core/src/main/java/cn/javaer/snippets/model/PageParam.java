package cn.javaer.snippets.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;

/**
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

    PageParam(final int page, final int size) {
        this.page = Math.max(page, 1);
        this.size = Math.max(size, 1);
    }

    @Schema(name = "page", description = "分页-页码", minimum = "1", defaultValue = "1")
    int page;

    @Schema(name = "size", description = "分页-大小", minimum = "1", defaultValue = "20")
    int size;

    public static PageParam of(final int page, final int size) {
        return new PageParam(page, size);
    }

    public static PageParam of(final Pageable pageable) {
        return new PageParam(pageable.getPageNumber(), pageable.getPageSize());
    }

    @Hidden
    public long getOffset() {
        return (long) (this.page - 1) * (long) this.size;
    }
}