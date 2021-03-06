package cn.javaer.snippets.kryo.serializers.eclipse.collections;

import cn.javaer.snippets.kryo.KryoPool;
import lombok.Data;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author cn-src
 */
class MutableListSerializerTest {
    private final KryoPool kryoPool = new KryoPool(kryo -> {
        kryo.register(User.class);
    });

    @Test
    void serializer() throws Exception {
        final User user = new User(Lists.mutable.of("name1"));
        final byte[] bytes = this.kryoPool.writeClassAndObject(user);
        final User userWithRead = this.kryoPool.readClassAndObject(bytes);
        assertThat(userWithRead.names).hasSize(1);
        assertThat(userWithRead.names.get(0)).isEqualTo("name1");
    }

    @Data
    private static class User {
        MutableList<String> names;

        public User() {
        }

        public User(final MutableList<String> names) {
            this.names = names;
        }
    }
}