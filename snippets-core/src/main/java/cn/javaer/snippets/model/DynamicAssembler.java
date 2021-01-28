package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Value;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collections;
import java.util.Map;

/**
 * @author cn-src
 */
@Value
public class DynamicAssembler<T1, T2> {
    @JsonUnwrapped
    T1 unwrapped1;
    Map<String, T2> dynamic;

    DynamicAssembler(final T1 unwrapped1, final Map<String, T2> dynamic) {
        this.unwrapped1 = unwrapped1;
        this.dynamic = dynamic;
    }

    @UnmodifiableView
    @JsonAnyGetter
    public Map<String, T2> getDynamic() {
        return Collections.unmodifiableMap(this.dynamic);
    }
}
