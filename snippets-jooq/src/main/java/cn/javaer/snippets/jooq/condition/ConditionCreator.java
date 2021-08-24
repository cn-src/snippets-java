package cn.javaer.snippets.jooq.condition;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.javaer.snippets.jooq.condition.annotation.BiCondition;
import cn.javaer.snippets.jooq.condition.annotation.ConditionIgnore;
import cn.javaer.snippets.jooq.condition.annotation.ConditionTree;
import cn.javaer.snippets.model.TreeNode;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    public static Condition create(final TreeNode treeNode,
                                   @NotNull final Field<String>... fields) {
        if (null == treeNode) {
            return null;
        }
        return create(Collections.singletonList(treeNode), fields);
    }

    @Nullable
    @SafeVarargs
    public static Condition create(final List<TreeNode> treeNodes,
                                   @NotNull final Field<String>... fields) {
        Objects.requireNonNull(fields);
        if (CollUtil.isEmpty(treeNodes)) {
            return null;
        }
        TreeNode current = TreeNode.of("", treeNodes);
        final LinkedList<TreeNode> stack = new LinkedList<>();
        stack.push(current);

        Condition condition = null;
        while (null != current) {
            if (stack.size() <= fields.length && !CollUtil.isEmpty(current.getChildren())) {
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
                    peek.removeFirstChild();
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
                        peek.removeFirstChild();
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

        final PropertyDescriptor[] pds = BeanUtil.getPropertyDescriptors(query.getClass());
        final List<Condition> conditions = new ArrayList<>(pds.length);

        final Map<String, Pair> biMap = new HashMap<>(3);
        for (final PropertyDescriptor pd : pds) {
            final java.lang.reflect.Field fd =
                ReflectUtil.getField(query.getClass(), pd.getName());
            if (fd == null || AnnotatedElementUtils.isAnnotated(fd, ConditionIgnore.class)) {
                continue;
            }

            final cn.javaer.snippets.jooq.condition.annotation.Condition conditionAnn =
                AnnotatedElementUtils.findMergedAnnotation(fd,
                    cn.javaer.snippets.jooq.condition.annotation.Condition.class);
            final BiCondition biConditionAnn =
                AnnotatedElementUtils.findMergedAnnotation(fd, BiCondition.class);
            final ConditionTree conditionTree =
                AnnotatedElementUtils.findMergedAnnotation(fd, ConditionTree.class);
            if (ignoreUnannotated && conditionAnn == null
                && biConditionAnn == null && conditionTree == null) {
                continue;
            }

            final Object value = ReflectionUtils.invokeMethod(pd.getReadMethod(), query);
            if (!ObjectUtil.isEmpty(value)) {
                if (conditionTree != null) {
                    final Field[] fields = Arrays.stream(conditionTree.value())
                        .map(DSL::field).toArray(Field[]::new);
                    if (value instanceof List) {
                        conditions.add(create((List) value, fields));
                    }
                    else {
                        conditions.add(create((List) Collections.singletonList(value), fields));
                    }
                }
                else if (null != biConditionAnn) {
                    final String column = biConditionAnn.column();
                    Assert.notEmpty(column, () ->
                        new IllegalArgumentException("'column' must be not empty"));
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
                    conditions.add(fun.apply(createField(pd.getName(), value.getClass()), value));
                }
            }
        }
        if (conditions.isEmpty() && biMap.isEmpty()) {
            return null;
        }
        Condition condition = null;
        for (final Condition con : conditions) {
            condition = condition == null ? con : condition.and(con);
        }
        for (final Map.Entry<String, Pair> entry : biMap.entrySet()) {
            final Pair pair = entry.getValue();
            final Field<Object> column = DSL.field(underline(entry.getKey()));
            final Condition con = pair.operator.getFunction().apply(
                column, pair.getMin(), pair.getMax());
            condition = condition == null ? con : condition.and(con);
        }
        return condition;
    }

    private static Field<Object> createField(final String name, final Class<?> clazz) {
        if (JSONB.class.equals(clazz)) {
            return (Field) DSL.field(underline(name), SQLDataType.JSONB);
        }
        return DSL.field(underline(name));
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