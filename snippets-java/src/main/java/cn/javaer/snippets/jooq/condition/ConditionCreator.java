package cn.javaer.snippets.jooq.condition;

import cn.javaer.snippets.jooq.condition.annotation.BiCondition;
import cn.javaer.snippets.jooq.condition.annotation.ConditionIgnore;
import cn.javaer.snippets.model.TreeNode;
import lombok.Data;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Condition 条件创建器，根据 POJO 来动态创建条件.
 *
 * @author cn-src
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConditionCreator {

    @Nullable
    public static Condition create(final Object query) {
        return ConditionCreator.create(query, false);
    }

    @Nullable
    public static Condition createWithIgnoreUnannotated(final Object query) {
        return ConditionCreator.create(query, true);
    }

    @Nullable
    @SafeVarargs
    public static Condition create(final List<TreeNode> treeNodes,
                                   @NonNull final Field<String>... fields) {
        Objects.requireNonNull(fields);
        if (CollectionUtils.isEmpty(treeNodes)) {
            return null;
        }
        TreeNode current = TreeNode.of("", treeNodes);
        final LinkedList<TreeNode> stack = new LinkedList<>();
        stack.push(current);

        Condition condition = null;
        while (null != current) {
            if (stack.size() <= fields.length && !CollectionUtils.isEmpty(current.getChildren())) {
                current = current.getChildren().get(0);
                stack.push(current.clone());
            }
            else {
                final Condition eq = fields[stack.size() - 2].eq(current.getTitle());
                if (null == condition) {
                    condition = eq;
                }
                else {
                    condition = condition.or(eq);
                }

                stack.pop();

                TreeNode peek = stack.peek();
                if (peek != null) {
                    peek.getChildren().remove(0);
                }

                // 迭代清理一条线的所有孤叶节点
                while (peek != null && (peek.getChildren() == null || peek.getChildren().isEmpty())) {
                    final TreeNode pop = stack.pop();
                    if (!stack.isEmpty()) {
                        condition = condition.and(fields[stack.size() - 1].eq(pop.getTitle()));
                    }
                    if (stack.isEmpty()) {
                        peek = null;
                        break;
                    }
                    peek = stack.peek();
                    if (peek.getChildren() != null && !peek.getChildren().isEmpty()) {
                        peek.getChildren().remove(0);
                    }
                }
                current = peek;
            }
        }
        return condition;
    }

    @Nullable
    private static Condition create(final Object query, final boolean ignoreUnannotated) {
        if (query == null) {
            return null;
        }
        final PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(query.getClass());
        final List<Condition> conditions = new ArrayList<>(pds.length);

        final Map<String, Pair> biMap = new HashMap<>();
        for (final PropertyDescriptor pd : pds) {
            final java.lang.reflect.Field fd =
                ReflectionUtils.findField(query.getClass(), pd.getName());
            if (fd == null || AnnotatedElementUtils.isAnnotated(fd, ConditionIgnore.class)) {
                continue;
            }

            final cn.javaer.snippets.jooq.condition.annotation.Condition conditionAnn =
                AnnotatedElementUtils.findMergedAnnotation(fd,
                    cn.javaer.snippets.jooq.condition.annotation.Condition.class);
            final BiCondition biConditionAnn =
                AnnotatedElementUtils.findMergedAnnotation(fd, BiCondition.class);
            if (ignoreUnannotated && conditionAnn == null && biConditionAnn == null) {
                continue;
            }

            final Object value = ReflectionUtils.invokeMethod(pd.getReadMethod(), query);
            if (!ObjectUtils.isEmpty(value)) {
                if (null != biConditionAnn) {
                    final String column = biConditionAnn.column();
                    Assert.hasLength(column, () -> "'column' must be not empty");
                    final Pair pair = biMap.computeIfAbsent(column, s -> new Pair());
                    if (pair.operator == null) {
                        pair.operator = biConditionAnn.operator();
                    }
                    else {
                        Assert.state(pair.operator.equals(biConditionAnn.operator()),
                            () -> "'operator' must be the same");
                    }
                    if (BiCondition.ValueType.MIN.equals(biConditionAnn.valueType())) {
                        biMap.computeIfAbsent(column, s -> new Pair()).min = value;
                    }
                    else {
                        biMap.computeIfAbsent(column, s -> new Pair()).max = value;
                    }
                }
                else {
                    final BiFunction<Field<Object>, Object, Condition> fun =
                        conditionAnn == null ? Field::equal : conditionAnn.value().getFunction();
                    final Field<Object> column = DSL.field(underline(pd.getName()));
                    conditions.add(fun.apply(column, value));
                }
            }
        }
        if (conditions.isEmpty()) {
            return null;
        }
        Condition condition = conditions.get(0);
        for (int i = 1, size = conditions.size(); i < size; i++) {
            condition = condition.and(conditions.get(i));
        }
        for (final Map.Entry<String, Pair> entry : biMap.entrySet()) {
            final Pair pair = entry.getValue();
            final Field<Object> column = DSL.field(underline(entry.getKey()));
            condition = condition.and(pair.operator.getFunction().apply(column, pair.getMin(),
                pair.getMax()));
        }
        return condition;
    }

    private static String underline(final String str) {
        final char[] chars = str.toCharArray();
        final StringBuilder sb = new StringBuilder(chars.length);
        for (final char c : chars) {
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Data
    static class Pair {
        BiCondition.Operator operator;
        Object min;
        Object max;
    }
}
