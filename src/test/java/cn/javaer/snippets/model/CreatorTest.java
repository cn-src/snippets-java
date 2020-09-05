package cn.javaer.snippets.model;

import cn.javaer.snippets.jackson.Json;
import lombok.Value;
import org.junit.jupiter.api.Test;

/**
 * @author cn-src
 */
class CreatorTest {

    @Test
    void of() {
        final String json = Json.DEFAULT.write(Creator.of(new Demo("d1"), new User("n1")));
        System.out.println(json);
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