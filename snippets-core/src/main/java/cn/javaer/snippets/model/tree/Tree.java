package cn.javaer.snippets.model.tree;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * @author cn-src
 */
public class Tree {
    public static <E> Collection<Node> of(final List<E> models, Conf<E> conf) {

        if (CollUtil.isEmpty(models)) {
            return Collections.emptySet();
        }

        final Node root = Node.of("");
        Node current = root;
        List<Node> call = new ArrayList<>(100);
        for (final E row : models) {
            int depth = 1;
            final String[] names = conf.nameFun.apply(row);
            for (final String name : names) {
                if (current.childrenMap.containsKey(name)) {
                    current = current.childrenMap.get(name);
                }
                else if (!(conf.ignoreEmpty && (StrUtil.isEmpty(name)))) {
                    final Node node = Node.of(name, conf.sortFun.apply(row));
                    final NodeInfo<E> nodeInfo = new NodeInfo<>(row, node, depth,
                        current.childrenMap.size(), node.dynamic);
                    conf.handler.apply(nodeInfo);
                    current.childrenMap.put(name, node);
                    call.add(node);
                    current = node;
                }
                depth++;
            }
            current = root;
        }
        for (Node n : call) {
            n.moveToChildren();
        }
        return root.getChildren();
    }

    public static <E> List<E> toModel(final List<Node> treeNodes,
                                      final Function<List<String>, E> resultFun) {
        Objects.requireNonNull(resultFun);

        if (CollUtil.isEmpty(treeNodes)) {
            return Collections.emptyList();
        }

        final List<E> result = new ArrayList<>();
        Node current = Node.of("", treeNodes);
        ArrayList<Node> stack = new ArrayList<>();
        stack.add(current);

        // 遍历树结构，转换成二维表结构用于存库，深度优先遍历
        // 从根节点，遍历到叶子节点，为数据库一条记录，同时移除此叶子节点
        // 当前迭代的节点往根节点方向，以及同级的下级节点移动
        while (null != current) {
            if (CollUtil.isNotEmpty(current.getChildren())) {
                current = current.children.get(0);
                stack.add(Node.of(current.name, current.getChildren()));
            }
            else {
                final int size = stack.size();
                final List<String> names = new ArrayList<>(size - 1);
                for (int i = 1; i < size; i++) {
                    names.add(stack.get(i).getName());
                }
                result.add(resultFun.apply(names));
                stack.remove(stack.size() - 1);

                Node peek = CollUtil.get(stack, stack.size() - 1);
                if (peek != null) {
                    peek.removeFirstChild();
                }

                // 迭代清理一条线的所有孤叶节点
                while (peek != null && (CollUtil.isEmpty(peek.getChildren()))) {
                    stack.remove(stack.size() - 1);
                    if (stack.isEmpty()) {
                        peek = null;
                        break;
                    }
                    peek = CollUtil.get(stack, stack.size() - 1);
                    if (peek.getChildren() != null && !peek.getChildren().isEmpty()) {
                        peek.removeFirstChild();
                    }
                }
                current = peek;
            }
        }
        return result;
    }

    public static class Node implements Comparable<Node> {
        @Getter
        private final String name;

        private final List<Node> children;

        private TreeMap<String, Node> childrenMap;

        private final Long sort;

        private final Map<String, Object> dynamic = new HashMap<>();

        private Node(String name, List<Node> children, Long sort) {
            this.name = name;
            this.children = children;
            this.sort = sort;
            this.childrenMap = new TreeMap<>();
        }

        public static Node of(String name) {
            return new Node(name, new ArrayList<>(), null);
        }

        public static Node of(String name, Long sort) {
            return new Node(name, new ArrayList<>(), sort);
        }

        public static Node of(String name, List<Node> children) {
            return new Node(name, new ArrayList<>(children), null);
        }

        public static Node of(String name, List<Node> children, Long sort) {
            return new Node(name, new ArrayList<>(children), sort);
        }

        void removeFirstChild() {
            if (!this.children.isEmpty()) {
                this.children.remove(0);
            }
        }

        void moveToChildren() {
            this.children.addAll(childrenMap.values());
            this.childrenMap = null;
        }

        @Override
        public int compareTo(@NotNull Tree.Node node) {
            return CompareUtil.compare(this.sort, node.sort);
        }

        @UnmodifiableView
        public List<Node> getChildren() {
            return Collections.unmodifiableList(children);
        }

        @JsonAnyGetter
        @UnmodifiableView
        public Map<String, Object> getDynamic() {
            return Collections.unmodifiableMap(this.dynamic);
        }
    }

    @Value
    public static class NodeInfo<E> {
        E model;
        Node node;
        int depth;
        int index;
        Map<String, Object> dynamic;
    }

    @Value
    @Builder
    public static class Conf<E> {

        Function<E, String[]> nameFun;

        Function<E, Long> sortFun;

        NodeHandler<E> handler;

        boolean ignoreEmpty;
    }

    public interface NodeHandler<E> {
        /**
         * TreeNode 自定义处理.
         *
         * @param nodeInfo nodeInfo
         */
        void apply(NodeInfo<E> nodeInfo);
    }
}