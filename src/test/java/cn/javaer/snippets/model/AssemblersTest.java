package cn.javaer.snippets.model;

import cn.javaer.snippets.jackson.Json;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author cn-src
 */
class AssemblersTest {
    @Test
    void serialization() throws Exception {
        final Assembler<User, Demo> assembler = Assemblers.of(new User("u1"), new Demo("d1"));
        final DynamicAssembler<User, User> dynamicAssembler =
            Assemblers.ofDynamic(new User("u1"), "k1", new User("u2"));

        JSONAssert.assertEquals("{\"name\":\"u1\",\"demo\":\"d1\"}",
            Json.DEFAULT.write(assembler), true);

        JSONAssert.assertEquals("{\"name\":\"u1\",\"k1\":{\"name\":\"u2\"}}",
            Json.DEFAULT.write(dynamicAssembler), true);
    }

    @Value
    static class User {
        String name;
    }

    @Value
    static class Demo {
        String demo;
    }
}