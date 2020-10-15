package cn.javaer.snippets.jooq.condition;

import cn.javaer.snippets.model.TreeNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.Field;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Condition 条件构建器, 会忽略一些空值的条件.
 *
 * @author cn-src
 */
public class ConditionBuilder {

    private final List<Condition> conditions = new ArrayList<>();

    public ConditionBuilder append(final Condition condition) {
        if (null != condition) {
            this.conditions.add(condition);
        }
        return this;
    }

    public ConditionBuilder append(final boolean isAppend, final Condition condition) {
        if (isAppend) {
            this.conditions.add(condition);
        }
        return this;
    }

    public ConditionBuilder isNotNull(final Field<?>... fields) {
        if (null != fields && fields.length > 0) {
            if (fields.length == 1) {
                this.conditions.add(fields[0].isNotNull());
            }
            else {
                Arrays.stream(fields).forEach(field -> this.conditions.add(field.isNotNull()));
            }
        }
        return this;
    }

    public ConditionBuilder append(@NotNull final Supplier<@Nullable Condition> supplier) {
        this.append(supplier.get());
        return this;
    }

    public ConditionBuilder required(@NotNull final Supplier<@NotNull Condition> supplier) {
        this.conditions.add(Objects.requireNonNull(supplier.get()));
        return this;
    }

    @SafeVarargs
    public final ConditionBuilder append(final List<TreeNode> treeNodes,
                                         @NotNull final Field<String>... fields) {
        return this.append(ConditionCreator.create(treeNodes, fields));
    }

    public <T> ConditionBuilder append(@NotNull final Function<T, Condition> fun, final T value) {
        if (ObjectUtils.isEmpty(value)) {
            return this;
        }

        this.conditions.add(fun.apply(value));
        return this;
    }

    public <T1, T2> ConditionBuilder append(@NotNull final BiFunction<T1, T2, Condition> fun,
                                            final T1 t1, final T2 t2) {
        if (ObjectUtils.isEmpty(t1) || ObjectUtils.isEmpty(t2)) {
            return this;
        }
        this.conditions.add(fun.apply(t1, t2));
        return this;
    }

    public ConditionBuilder dateTime(@NotNull final DataTimeBiFunction fun,
                                     final LocalDate start, final LocalDate end) {
        if (null == start && null == end) {
            return this;
        }
        final LocalDateTime startTime = Objects.requireNonNull(start).atTime(LocalTime.MIN);
        final LocalDateTime endTime = Objects.requireNonNull(end).atTime(LocalTime.MAX);
        this.conditions.add(fun.apply(startTime, endTime));
        return this;
    }

    public ConditionBuilder dateTime(@NotNull final DataTimeBiFunction fun,
                                     final LocalDateTime start, final LocalDateTime end) {
        if (null == start && null == end) {
            return this;
        }
        this.conditions.add(fun.apply(start, end));
        return this;
    }

    public <T1, T2, T3> ConditionBuilder append(@NotNull final Function3<T1, T2, T3> fun,
                                                final T1 t1, final T2 t2, final T3 t3) {
        if (ObjectUtils.isEmpty(t1) || ObjectUtils.isEmpty(t2) || ObjectUtils.isEmpty(t3)) {
            return this;
        }
        this.conditions.add(fun.apply(t1, t2, t3));
        return this;
    }

    @Nullable
    public Condition build() {
        if (this.conditions.isEmpty()) {
            return null;
        }
        Condition condition = this.conditions.get(0);
        for (int i = 1, size = this.conditions.size(); i < size; i++) {
            condition = condition.and(this.conditions.get(i));
        }
        return condition;
    }

    @FunctionalInterface
    public interface Function3<T1, T2, T3> {
        /**
         * The three parameters Function.
         *
         * @param t1 t1
         * @param t2 t2
         * @param t3 t3
         *
         * @return Condition
         */
        Condition apply(T1 t1, T2 t2, T3 t3);
    }

    @FunctionalInterface
    public interface DataTimeBiFunction {
        Condition apply(LocalDateTime t1, LocalDateTime t2);
    }
}
