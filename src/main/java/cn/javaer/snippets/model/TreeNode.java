package cn.javaer.snippets.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 树节点.
 *
 * @author cn-src
 */
@Data
public class TreeNode {
    private String title;
    private List<TreeNode> children;

    @JsonAnySetter
    private Map<String, Object> dynamic;

    public TreeNode() {
    }

    public TreeNode(final String title) {
        this.title = title;
    }

    public final void addChildren(final TreeNode... child) {
        if (child == null || child.length == 0) {
            return;
        }
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.addAll(Arrays.asList(child));
    }

    @JsonAnyGetter
    public Map<String, Object> getDynamic() {
        return this.dynamic;
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
