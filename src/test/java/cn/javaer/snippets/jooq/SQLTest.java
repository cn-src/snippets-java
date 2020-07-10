package cn.javaer.snippets.jooq;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class SQLTest {

    DSLContext dsl = DSL.using(SQLDialect.POSTGRES);

    @Test
    void stContains() {

        Field<Boolean> stContains = SQL.stContains(DSL.field("geom_a", SQL.GEOMETRY_TYPE), DSL.field("geom_b", SQL.GEOMETRY_TYPE));
        assertThat(this.dsl.renderInlined(stContains))
                .isEqualTo("ST_Contains(geom_a, geom_b)");
    }

    @Test
    void stAsGeoJSON() {
        Field<String> stAsGeoJSON = SQL.stAsGeoJSON(DSL.field("geom", SQL.GEOMETRY_TYPE));
        assertThat(this.dsl.renderInlined(stAsGeoJSON))
                .isEqualTo("ST_AsGeoJSON(geom)");
    }
}