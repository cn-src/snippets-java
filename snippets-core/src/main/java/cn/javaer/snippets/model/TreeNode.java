package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

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
    public static final TreeNode EMPTY = TreeNode.of("");

    @Setter(AccessLevel.PROTECTED)
    private @Nullable String title;

    @Setter(AccessLevel.PROTECTED)
    private @Nullable List<TreeNode> children;

    private @Nullable Map<String, Object> dynamic;

    @SuppressWarnings("unused")
    TreeNode() {
    }

    TreeNode(final @Nullable String title, final @Nullable List<TreeNode> children,
             final @Nullable Map<String, Object> dynamic) {
        this.title = title;
        this.children = children;
        this.dynamic = dynamic;
    }

    public static TreeNode of(final String title) {
        return new TreeNode(title, null, null);
    }

    public static TreeNode of(final String title, final List<TreeNode> children) {
        return new TreeNode(title, children == null ? null : new ArrayList<>(children), null);
    }

    @SuppressWarnings("UnusedReturnValue")
    public TreeNode addChildren(final TreeNode... children) {
        if (children == null || children.length == 0) {
            return this;
        }

        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.addAll(Arrays.asList(children));
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public TreeNode removeFirstChild() {
        if (this.children == null || this.children.isEmpty()) {
            return this;
        }
        this.children.remove(0);
        return this;
    }

    @JsonAnySetter
    public TreeNode putDynamic(final String key, final Object value) {
        if (this.dynamic == null) {
            this.dynamic = new HashMap<>(5);
        }
        this.dynamic.put(key, value);
        return this;
    }

    public TreeNode putAllDynamic(final Map<String, Object> dynamic) {
        if (dynamic == null || dynamic.isEmpty()) {
            return this;
        }

        if (this.dynamic == null) {
            this.dynamic = new HashMap<>(dynamic.size());
        }
        this.dynamic.putAll(dynamic);
        return this;
    }

    @JsonIgnore
    public boolean isEmptyChildren() {
        return this.children == null || this.children.isEmpty();
    }

    @JsonAnyGetter
    @UnmodifiableView
    public @Nullable Map<String, Object> getDynamic() {
        return this.dynamic == null ? null : Collections.unmodifiableMap(this.dynamic);
    }

    public @Nullable String getTitle() {
        return this.title;
    }

    @UnmodifiableView
    public @Nullable List<TreeNode> getChildren() {
        return this.children == null ? null : Collections.unmodifiableList(this.children);
    }

    @SuppressWarnings({"MethodDoesntCallSuperMethod"})
    @Override
    public TreeNode clone() {
        return new TreeNode(this.title, this.children, this.dynamic);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TreeNode.class.getSimpleName() + "[", "]")
            .add("title=" + this.title)
            .add("@children.size=" + (this.children == null ? 0 : this.children.size()))
            .add("dynamic=" + this.dynamic)
            .toString();
    }
}