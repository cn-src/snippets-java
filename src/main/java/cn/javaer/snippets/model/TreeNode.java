package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 树节点.
 *
 * @author cn-src
 */
@EqualsAndHashCode
public class TreeNode implements Cloneable {
    public static final TreeNode EMPTY = new TreeNode("",
        Collections.emptyList(), Collections.emptyMap());
    private final String title;
    private final List<TreeNode> children;

    private @JsonAnySetter final Map<String, Object> dynamic;

    @JsonCreator
    @ConstructorProperties({"title", "children", "dynamic"})
    public TreeNode(final String title, final List<TreeNode> children,
                    final Map<String, Object> dynamic) {
        this.title = title;
        this.children = children == null ? new ArrayList<>() : new ArrayList<>(children);
        this.dynamic = dynamic == null ? new HashMap<>() : new HashMap<>(dynamic);
    }

    public static TreeNode of(final String title) {
        return new TreeNode(title, new ArrayList<>(), new HashMap<>());
    }

    public static TreeNode of(final String title, final List<TreeNode> children) {
        return new TreeNode(title, children, new HashMap<>());
    }

    public final TreeNode addChildren(final TreeNode first, final TreeNode... child) {
        this.children.add(first);

        if (child == null || child.length == 0) {
            return this;
        }
        this.children.addAll(Arrays.asList(child));
        return this;
    }

    public final TreeNode putDynamic(final String key, final Object value) {
        this.dynamic.put(key, value);
        return this;
    }

    public final TreeNode putAllDynamic(final Map<String, Object> dynamic) {
        this.dynamic.putAll(dynamic);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getDynamic() {
        return this.dynamic;
    }

    public String getTitle() {
        return this.title;
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod",
        "CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    protected TreeNode clone() {
        return new TreeNode(this.title, this.children, this.dynamic);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TreeNode.class.getSimpleName() + "[", "]")
            .add("title=" + this.title)
            .add("@children.size=" + this.children.size())
            .add("dynamic=" + this.dynamic)
            .toString();
    }
}
