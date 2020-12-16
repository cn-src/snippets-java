package cn.javaer.snippets.jooq;

import java.util.Optional;

public interface AuditorAware<T> {

    Optional<T> getCurrentAuditor();

}