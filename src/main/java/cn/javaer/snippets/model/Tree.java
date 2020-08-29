package cn.javaer.snippets.model;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 树结构.
 *
 * @author cn-src
 */
public interface Tree {

    @SuppressWarnings("unchecked")
    @SafeVarargs
    static <E> List<TreeNode> of(final List<E> models,
                                 final Function<E, String>... getters) {
        return of(models, TreeNodeHandler.EMPTY, getters);
    }

    /**
     * 将实体列表数据转换成树结构，比如多级区域数据转换成前端 UI 组件需要的 Tree 结构.
     *
     * @param models 二维表结构的实体数据
     * @param handler 额外的附加处理，
     * @param getters 实体的哪些字段 getter 用于转换成树
     * @param <E> 实体类型
     *
     * @return 根节点的所有子节点
     */
    @SafeVarargs
    static <E> List<TreeNode> of(final List<E> models,
                                 final TreeNodeHandler<E> handler,
                                 final Function<E, String>... getters) {
        Objects.requireNonNull(getters);

        if (models == null || models.isEmpty()) {
            return Collections.emptyList();
        }

        final TreeNode root = TreeNode.of("");
        TreeNode current = root;

        for (final E row : models) {
            int depth = 1;
            for (final Function<E, String> fn : getters) {
                final String cell = fn.apply(row);

                final Optional<TreeNode> first = current.getChildren().stream()
                    .filter(it -> Objects.equals(cell, it.getTitle()))
                    .findFirst();
                if (first.isPresent()) {
                    current = first.get();
                }
                else {
                    final TreeNode treeNode = TreeNode.of(cell);
                    handler.apply(treeNode, row, depth, current.getChildren().size());
                    current.getChildren().add(treeNode);
                    current = treeNode;
                }
                depth++;
            }
            current = root;
        }
        return root.getChildren();
    }

    /**
     * 将 Tree 节点数据转换成二维表结构.
     *
     * @param treeNodes Tree 节点数据
     * @param createFn 实体类创建函数
     * @param setters 实体类 setter 函数
     * @param <E> 实体类型
     *
     * @return 实体列表
     */
    @SafeVarargs
    static <E> List<E> toModel(final List<TreeNode> treeNodes, final Supplier<E> createFn,
                               final BiConsumer<E, String>... setters) {
        Objects.requireNonNull(createFn);
        Objects.requireNonNull(setters);

        if (treeNodes == null || treeNodes.isEmpty()) {
            return Collections.emptyList();
        }

        final List<E> result = new ArrayList<>();
        TreeNode current = TreeNode.of("", treeNodes);
        final LinkedList<TreeNode> stack = new LinkedList<>();
        stack.push(current);

        // 遍历树结构，转换成二维表结构用于存库，深度优先遍历
        // 从根节点，遍历到叶子节点，为数据库一条记录，同时移除此叶子节点
        // 当前迭代的节点往根节点方向，以及同级的下级节点移动
        while (null != current) {
            if (stack.size() <= setters.length && !CollectionUtils.isEmpty(current.getChildren())) {
                current = current.getChildren().get(0);
                stack.push(current.clone());
            }
            else {
                final int size = stack.size() - 1;
                final E e = createFn.get();
                for (int i = 0; i < size; i++) {
                    setters[i].accept(e, stack.get(size - 1 - i).getTitle());
                }
                result.add(e);
                stack.pop();

                TreeNode peek = stack.peek();
                if (peek != null) {
                    peek.getChildren().remove(0);
                }

                // 迭代清理一条线的所有孤叶节点
                while (peek != null && (peek.getChildren() == null || peek.getChildren().isEmpty())) {
                    stack.pop();
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
        return result;
    }
}