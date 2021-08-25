package cn.javaer.snippets.spring.util;

import cn.javaer.snippets.model.PageParam;
import org.springframework.data.domain.Pageable;

/**
 * @author cn-src
 */
public interface PageUtils {

    static PageParam of(final Pageable pageable) {
        return PageParam.of(pageable.getPageNumber() + 1, pageable.getPageSize());
    }
}