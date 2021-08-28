package cn.javaer.snippets.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author cn-src
 */
public class Tree {
    public static <E> List<TreeNode> of(final List<E> models, TreeConf<E> treeConf) {

        if (CollUtil.isEmpty(models)) {
            return Collections.emptyList();
        }

        final TreeNode root = TreeNode.of("");
        TreeNode current = root;
        List<TreeNode> call = new ArrayList<>(50);
        for (final E row : models) {
            int depth = 1;
            final String[] names = treeConf.getNameFun().apply(row);
            for (final String name : names) {
                if (current.childrenMap.containsKey(name)) {
                    current = current.childrenMap.get(name);
                }
                else if (!(treeConf.isIgnoreEmpty() && (StrUtil.isEmpty(name)))) {
                    final TreeNode treeNode = TreeNode.of(name, treeConf.getSortFun().apply(row));
                    final int size = current.childrenMap.size();
                    final TreeInfo<E> TreeInfo = new TreeInfo<>(row, treeNode, depth,
                        size, size == names.length, treeNode.dynamic);
                    treeConf.getHandler().apply(TreeInfo);
                    current.childrenMap.put(name, treeNode);
                    call.add(treeNode);
                    current = treeNode;
                }
                depth++;
            }
            current = root;
        }
        for (TreeNode n : call) {
            n.moveToChildren();
        }
        root.moveToChildren();
        return root.getChildren();
    }

    public static <E> List<E> toModel(final List<TreeNode> treeTreeNodes,
                                      final Function<List<String>, E> resultFun) {
        Objects.requireNonNull(resultFun);

        if (CollUtil.isEmpty(treeTreeNodes)) {
            return Collections.emptyList();
        }

        final List<E> result = new ArrayList<>();
        TreeNode current = TreeNode.of("", treeTreeNodes);
        ArrayList<TreeNode> stack = new ArrayList<>();
        stack.add(current);

        // 遍历树结构，转换成二维表结构用于存库，深度优先遍历
        // 从根节点，遍历到叶子节点，为数据库一条记录，同时移除此叶子节点
        // 当前迭代的节点往根节点方向，以及同级的下级节点移动
        while (null != current) {
            if (CollUtil.isNotEmpty(current.getChildren())) {
                current = current.children.get(0);
                stack.add(TreeNode.of(current.name, current.getChildren()));
            }
            else {
                final int size = stack.size();
                final List<String> names = new ArrayList<>(size - 1);
                for (int i = 1; i < size; i++) {
                    names.add(stack.get(i).getName());
                }
                result.add(resultFun.apply(names));
                // 迭代清理一条线的所有孤叶节点（没有子节点的节点 ）
                TreeNode peek;
                do {
                    // 移除当前已经使用的节点，以及当前节点在父节点的位置
                    stack.remove(stack.size() - 1);
                    peek = CollUtil.get(stack, stack.size() - 1);
                    if (peek != null) {
                        peek.removeFirstChild();
                    }
                } while (peek != null && (CollUtil.isEmpty(peek.getChildren())));
                current = peek;
            }
        }
        return result;
    }
}