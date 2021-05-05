package cn.javaer.snippets.jooq;

import lombok.Value;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class CrudStepWithH2Test {
    DSLContext dsl = null;
    CrudStep crudStep = null;

    @BeforeEach
    void setUp() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUser("sa");
        dataSource.setUrl("jdbc:h2:mem:" + UUID.randomUUID() +
            ";DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1");
        this.dsl = DSL.using(dataSource, SQLDialect.H2);
        //language=H2
        this.dsl.execute("CREATE TABLE demo (id bigint, name varchar)");
        //language=H2
        this.dsl.execute("CREATE TABLE CITY (ID bigint, NAME varchar)");
        this.crudStep = new CrudStep(this.dsl, (AuditorAware<Long>) () -> Optional.of(999L));

        this.dsl.meta().getTables().forEach(System.out::println);
    }

    @Test
    void oneStep() {
        final TableMeta<Demo, Long, Void> meta = CrudReflection.getTableMeta(Demo.class);
        this.crudStep.insertStep(meta, new Demo(1L, "name"))
            .execute();
        final Demo demo = this.crudStep.findByIdStep(meta, 1L).fetchOneInto(Demo.class);
        assertThat(demo).extracting(Demo::getId).isEqualTo(1L);
        assertThat(demo).extracting(Demo::getName).isEqualTo("name");
    }

    @Test
    void batchInsertStep() {
        final TableMeta<Demo, Long, Void> meta = CrudReflection.getTableMeta(Demo.class);
        this.crudStep.batchInsertStep(Arrays.asList(new Demo(1L, "name1"), new Demo(2L, "name2")),
            meta).execute();
    }

    @Test
    void oneMetaStep() {
        this.crudStep.insertStep(TCity.CITY, new City(1, "name"))
            .execute();
        final City city = this.crudStep.findByIdStep(TCity.CITY, 1).fetchOneInto(City.class);
        assertThat(city).extracting(City::getId).isEqualTo(1);
        assertThat(city).extracting(City::getName).isEqualTo("name");
    }

    @Value
    public static class Demo {
        @Id
        Long id;

        String name;
    }
}