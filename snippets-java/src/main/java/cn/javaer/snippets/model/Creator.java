package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Value;
import org.springframework.data.annotation.Immutable;

/**
 * 适用于需要展示创建者信息，不支持反序列化.
 *
 * @author cn-src
 */
@Value
@Immutable
public class Creator<T, C> {
    @JsonUnwrapped
    T unwrapped;
    C createdBy;

    Creator(final T unwrapped, final C createdBy) {
        this.unwrapped = unwrapped;
        this.createdBy = createdBy;
    }

    public static <T, C> Creator<T, C> of(final T unwrapped, final C createdBy) {
        return new Creator<>(unwrapped, createdBy);
    }
}
