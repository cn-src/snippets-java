package cn.javaer.snippets.spring.data.jooq;

import org.jooq.DSLContext;
import org.jooq.Query;

/**
 * @author cn-src
 */
public interface QueryStep {
    /**
     * 查询步骤.
     *
     * @param dsl DSLContext
     *
     * @return jOOQ Query
     */
    Query apply(DSLContext dsl);
}
