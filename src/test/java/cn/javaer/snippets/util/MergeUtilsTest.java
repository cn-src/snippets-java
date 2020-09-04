package cn.javaer.snippets.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author cn-src
 */
class MergeUtilsTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void mergeProperty() {
        final List<Demo> demos = Arrays.asList(
            new Demo("p0", null, null),
            new Demo("p1", 1L, null),
            new Demo("p2", 1L, null),
            new Demo("p3", 2L, null));

        final Prop p1 = new Prop(1L, "n1");
        final Prop p2 = new Prop(2L, "n2");
        final List<Prop> props = Arrays.asList(p1, p2);

        final List<Demo> result = MergeUtils.mergeProperty(demos, props, Demo::setDemoProp,
            Demo::getDemoPropId, Prop::getId);

        assertThat(result).extracting(Demo::getProp1, Demo::getDemoProp)
            .contains(
                tuple("p0", null),
                tuple("p1", p1),
                tuple("p2", p1),
                tuple("p3", p2)
            );
    }

    @Test
    void toOneToManyMap() throws Exception {
        final List<Product> products = Arrays.asList(
            new Product(1L, "n1", "c1-1", "c2-2", 2L),
            new Product(1L, "n1", "c1-1", "c2-2", 2L),
            new Product(2L, "n1", "c1-1", "c2-2", 2L),
            new Product(2L, "n1", "c1-2", "c2-2", 2L),
            new Product(3L, "n1", "c1-1", "c2-2", 2L)
        );
        final List<Product2> results = MergeUtils.mergePropertyToMap(
            products,
            Product::getId,
            Product2::getId,
            Product2::getDynamicData,
            Product2::setDynamicData,
            p -> String.format("%s-%s", p.getCategory1(), p.getCategory2()),
            (p, old) -> p.getCount() + (old == null ? 0 : old),
            p -> new Product2(p.getId(), p.getName())

        );
        //language=JSON
        JSONAssert.assertEquals("[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"n1\",\n" +
            "    \"dynamicData\": {\n" +
            "      \"c1-1-c2-2\": 4\n" +
            "    }\n" +
            "  }, {\n" +
            "  \"id\": 2,\n" +
            "  \"name\": \"n1\",\n" +
            "  \"dynamicData\": {\n" +
            "    \"c1-1-c2-2\": 2,\n" +
            "    \"c1-2-c2-2\": 2\n" +
            "  }\n" +
            "}, {\n" +
            "  \"id\": 3,\n" +
            "  \"name\": \"n1\",\n" +
            "  \"dynamicData\": {\n" +
            "    \"c1-1-c2-2\": 2\n" +
            "  }\n" +
            "}\n" +
            "]", this.objectMapper.writeValueAsString(results), false);
    }

    @Test
    void toOneToManyMap2() throws Exception {
        final List<Product> products = Arrays.asList(
            new Product(1L, "n1", "c1-1", "c2-2", 2L),
            new Product(1L, "n1", "c1-1", "c2-2", 2L),
            new Product(2L, "n1", "c1-1", "c2-2", 2L),
            new Product(2L, "n1", "c1-2", "c2-2", 2L),
            new Product(3L, "n1", "c1-1", "c2-2", 2L)
        );

        final List<Product> results = MergeUtils.mergePropertyToMap(
            products,
            Product::getId,
            Product::getDynamicData,
            Product::setDynamicData,
            p -> String.format("%s-%s", p.getCategory1(), p.getCategory2()),
            (p, old) -> p.getCount() + (old == null ? 0 : old)

        );
        //language=JSON
        JSONAssert.assertEquals("[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"n1\",\n" +
            "    \"category1\": \"c1-1\",\n" +
            "    \"category2\": \"c2-2\",\n" +
            "    \"count\": 2,\n" +
            "    \"dynamicData\": {\n" +
            "      \"c1-1-c2-2\": 4\n" +
            "    }\n" +
            "  }, {\n" +
            "  \"id\": 2,\n" +
            "  \"name\": \"n1\",\n" +
            "  \"category1\": \"c1-1\",\n" +
            "  \"category2\": \"c2-2\",\n" +
            "  \"count\": 2,\n" +
            "  \"dynamicData\": {\n" +
            "    \"c1-1-c2-2\": 2,\n" +
            "    \"c1-2-c2-2\": 2\n" +
            "  }\n" +
            "}, {\n" +
            "  \"id\": 3,\n" +
            "  \"name\": \"n1\",\n" +
            "  \"category1\": \"c1-1\",\n" +
            "  \"category2\": \"c2-2\",\n" +
            "  \"count\": 2,\n" +
            "  \"dynamicData\": {\n" +
            "    \"c1-1-c2-2\": 2\n" +
            "  }\n" +
            "}\n" +
            "]", this.objectMapper.writeValueAsString(results), false);
    }

    @Data
    @AllArgsConstructor
    static class Demo {
        String prop1;
        Long demoPropId;
        Prop demoProp;
    }

    @Data
    @AllArgsConstructor
    static class Prop {
        Long id;
        String name;
    }

    @Data
    @FieldNameConstants
    static class Product {
        final Long id;
        final String name;
        final String category1;
        final String category2;
        final Long count;
        Map<String, Long> dynamicData;
    }

    @Data
    @FieldNameConstants
    static class Product2 {
        final Long id;
        final String name;
        Map<String, Long> dynamicData;
    }
}