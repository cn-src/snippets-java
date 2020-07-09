package cn.javaer.snippets.jooq;

import cn.javaer.snippets.type.Geometry;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author cn-src
 */
class SQLTest {

    DSLContext dsl = DSL.using(SQLDialect.POSTGRES);

    @Test
    void stContains() {
//        Field<Boolean> stContains = SQL.stContains(DSL.field("geom_a", Geometry.class), DSL.field("geom_b", Geometry.class));
//        System.out.println(dsl.renderInlined(stContains));
    }

    @Test
    void stAsGeoJSON() {
    }
}