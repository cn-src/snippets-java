package cn.javaer.snippets.model;

/**
 * @author cn-src
 */
public interface TreeHandler<E> {
    /**
     * TreeNode 自定义处理.
     *
     * @param TreeInfo TreeNodeInfo
     */
    void apply(TreeInfo<E> TreeInfo);
}