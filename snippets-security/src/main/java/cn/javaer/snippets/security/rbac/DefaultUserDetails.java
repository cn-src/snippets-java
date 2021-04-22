package cn.javaer.snippets.security.rbac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author cn-src
 */
public interface DefaultUserDetails extends UserDetails {

    @JsonIgnore
    @Override
    default String getUsername() {
        return null;
    }

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
