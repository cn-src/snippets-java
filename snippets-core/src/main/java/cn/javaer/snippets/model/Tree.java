package cn.javaer.snippets.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
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

        List<E> es = new ArrayList<>(models);
        es.sort((o1, o2) -> {
            final Long t1 = treeConf.getSortFun().apply(o1);
            final Long t2 = treeConf.getSortFun().apply(o2);
            return CompareUtil.compare(t1, t2, true);
        });

        final TreeNode root = TreeNode.of("");
        TreeNode current = root;
        List<TreeNode> call = new ArrayList<>(50);
        for (final E row : es) {
            int depth = 1;
            final List<String> names = treeConf.getNamesFun().apply(row);
            int li = names.size() - 1;
            if (TreeConf.EmptyMode.NAMED_LEAF.equals(treeConf.getEmptyMode())) {
                li = CollUtil.lastIndexOf(names, StrUtil::isNotEmpty);
            }
            else if (TreeConf.EmptyMode.BREAK_EMPTY.equals(treeConf.getEmptyMode())) {
                li = CollUtil.indexOf(names, StrUtil::isEmpty) - 1;
            }
            int i = -1;
            for (final String name : names) {
                i++;
                if (li < 0 || i > li) {
                    break;
                }
                if (current.childrenMap.containsKey(name)) {
                    current = current.childrenMap.get(name);
                }
                else {
                    final TreeNode treeNode = TreeNode.of(name);
                    treeNode.treeInfo = new TreeInfo<>(treeNode, row, depth,
                        current.childrenMap.size());
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
            //noinspection unchecked
            treeConf.getHandler().apply(n.treeInfo);
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