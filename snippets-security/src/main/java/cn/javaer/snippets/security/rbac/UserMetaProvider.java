package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.jooq.TableMeta;

import java.util.Optional;

/**
 * @author cn-src
 */
public interface UserMetaProvider {
    default <T extends PersistableUser> TableMeta<T, ?, ?> meta() {
        @SuppressWarnings({"UnnecessaryLocalVariable", "unchecked"})
        final TableMeta<T, ?, ?> metaProvider =
            (TableMeta<T, ?, ?>) this.getMeta().orElseThrow(UnsupportedOperationException::new);
        return metaProvider;
    }

    <E extends PersistableUser>
    Optional<TableMeta<E, ?, ?>> getMeta();
}
