package cn.javaer.snippets.kryo;

import cn.javaer.snippets.spring.autoconfigure.eclipse.collections.pojo.City;
import org.junit.jupiter.api.Test;

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
}