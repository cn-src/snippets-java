package cn.javaer.snippets.security.rbac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author cn-src
 */
public interface DefaultUser extends UserDetails {

    /**
     * 获取手机号.
     *
     * @return 手机号
     */
    String getPhoneNumber();

    /**
     * 获取邮箱.
     *
     * @return 邮箱
     */
    String getEmail();

    @JsonIgnore
    @Override
    default boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    default boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    default boolean isCredentialsNonExpired() {
        return true;
    }
}