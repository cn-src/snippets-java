package cn.javaer.snippets.jooq;

import java.util.Optional;

public interface AuditorAware<T> {

    Optional<T> getCurrentAuditor();

    default T requiredAuditor() {
        return getCurrentAuditor().orElseThrow(() ->
            new IllegalStateException("Auditor must bu not null"));
    }
}