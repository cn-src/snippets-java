package cn.javaer.snippets.spring.data.jooq;

import org.jooq.DSLContext;
import org.jooq.Query;

/**
 * @author cn-src
 */
public interface QueryStep {
    Query apply(DSLContext dsl);
}
