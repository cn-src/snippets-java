package cn.javaer.snippets.security.rbac;

import cn.javaer.snippets.jooq.TableMeta;
import org.jooq.Field;

/**
 * @author cn-src
 */
public interface UserTableMeta<T extends UserEntity, ID, A> extends TableMeta<T, ID, A> {

    /**
     * 手机号字段.
     *
     * @return 手机号字段
     */
    Field<String> mobileField();

    /**
     * 邮箱字段.
     *
     * @return 邮箱字段
     */
    Field<String> emailField();
}
