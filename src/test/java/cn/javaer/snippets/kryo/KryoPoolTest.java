package cn.javaer.snippets.kryo;

import cn.javaer.snippets.kryo.serializers.ValueObjectSerializer;
import cn.javaer.snippets.spring.autoconfigure.eclipse.collections.pojo.City;
import lombok.Value;
import org.junit.jupiter.api.Test;

import java.beans.ConstructorProperties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class KryoPoolTest {
    @Test
    void name() {
        final KryoPool kryoPool = new KryoPool(kryo -> {
            kryo.register(City.class);
        });

        final byte[] bytes = kryoPool.writeClassAndObject(new City("n1"));
        final Object city = kryoPool.readClassAndObject(bytes);
        assertThat(city).hasFieldOrPropertyWithValue("name", "n1");
    }

    @Test
    void immutable() {
        final KryoPool kryoPool = new KryoPool(kryo -> {
            kryo.register(Demo.class, new ValueObjectSerializer<>(kryo, Demo.class));
        });

        final byte[] bytes = kryoPool.writeClassAndObject(new Demo("s1", "s2", new Demo("s1", "s2"
            , null)));
        final Object city = kryoPool.readClassAndObject(bytes);
        assertThat(city).hasFieldOrPropertyWithValue("str1", "s1");
    }

    @Value
    static class Demo {
        String str1;
        String str2;
        Demo demo;

        @ConstructorProperties({"str1", "str2", "demo"})
        public Demo(final String str1, final String str2, final Demo demo) {
            this.str1 = str1;
            this.str2 = str2;
            this.demo = demo;
        }
    }
}