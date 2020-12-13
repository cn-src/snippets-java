package cn.javaer.snippets.model;

/**
 * @author cn-src
 */
public interface TreeNodeHandler<T> {

    @SuppressWarnings("rawtypes") TreeNodeHandler EMPTY = (treeNode, t, depth, index) -> { };

    /**
     * TreeNode 自定义处理.
     *
     * @param treeNode TreeNode
     * @param t 模型的范型
     * @param depth 深度
     * @param index 索引
     */
    void apply(TreeNode treeNode, T t, int depth, int index);
}
