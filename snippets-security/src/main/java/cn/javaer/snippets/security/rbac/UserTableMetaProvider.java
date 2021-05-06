package cn.javaer.snippets.security.rbac;

/**
 * @author cn-src
 */
public interface UserTableMetaProvider {

    /**
     * 提供 UserTableMeta.
     *
     * @param <T> UserEntity
     *
     * @return UserTableMeta
     */
    <T extends UserEntity> UserTableMeta<T, ?, ?> meta();
}
