package cn.javaer.snippets.model;

import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * @author cn-src
 */
public class TreeNode implements Comparable<TreeNode> {
    @Getter final String name;

    final List<TreeNode> children;

    Map<String, TreeNode> childrenMap;

    @Getter
    Long sort;

    @JsonAnySetter final Map<String, Object> dynamic = new HashMap<>();

    private TreeNode(String name, List<TreeNode> children, Long sort) {
        this.name = name;
        this.children = children;
        this.sort = sort;
        this.childrenMap = new LinkedHashMap<>();
    }

    public static TreeNode of(String name, TreeNode... children) {
        return new TreeNode(name, ListUtil.toList(children), null);
    }

    public static TreeNode of(String name, Long sort) {
        return new TreeNode(name, new ArrayList<>(), sort);
    }

    public static TreeNode of(String name, List<TreeNode> children) {
        return new TreeNode(name, ListUtil.toList(children), null);
    }

    @JsonCreator
    public static TreeNode of(@JsonProperty("name") String name,
                              @JsonProperty("children") List<TreeNode> children,
                              @JsonProperty("sort") Long sort) {
        return new TreeNode(name, ListUtil.toList(children), sort);
    }

    void removeFirstChild() {
        if (!this.children.isEmpty()) {
            this.children.remove(0);
        }
    }

    void moveToChildren() {
        // TODO
        this.children.addAll(new TreeSet<>(childrenMap.values()));
        this.childrenMap = null;
    }

    @Override
    public int compareTo(@NotNull TreeNode node) {
        Long c1 = this.sort;
        Long c2 = node.sort;
        if (Objects.equals(c1, c2)) {
            // Tree 解析时是反序的，反转保障原始顺序。
            return 1;
        }
        if (c1 == null) {
            return -1;
        }
        if (c2 == null) {
            return 1;
        }
        return c1.compareTo(c2);
    }

    @UnmodifiableView
    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @JsonAnyGetter
    @UnmodifiableView
    public Map<String, Object> getDynamic() {
        return Collections.unmodifiableMap(this.dynamic);
    }
}