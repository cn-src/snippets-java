package cn.javaer.snippets.model;

/**
 * @author cn-src
 */
public interface TreeNodeHandler<T> {

    @SuppressWarnings("rawtypes") TreeNodeHandler EMPTY = (treeNode, t, depth, index) -> { };

    void apply(TreeNode treeNode, T t, int depth, int index);
}
