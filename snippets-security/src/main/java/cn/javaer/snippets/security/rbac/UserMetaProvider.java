package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.jooq.TableMetaProvider;

import java.util.Optional;

/**
 * @author cn-src
 */
public interface UserMetaProvider {
    default <T extends PersistableUser> TableMetaProvider<T, ?, ?> meta() {
        @SuppressWarnings({"UnnecessaryLocalVariable", "unchecked"})
        final TableMetaProvider<T, ?, ?> metaProvider =
            (TableMetaProvider<T, ?, ?>) this.getMeta().orElseThrow(UnsupportedOperationException::new);
        return metaProvider;
    }

    <E extends PersistableUser>
    Optional<TableMetaProvider<E, ?, ?>> getMeta();
}
