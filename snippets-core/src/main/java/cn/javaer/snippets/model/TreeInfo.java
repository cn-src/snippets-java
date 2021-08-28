package cn.javaer.snippets.model;

import lombok.Value;

import java.util.Map;

/**
 * @author cn-src
 */
@Value
public class TreeInfo<E> {
    E model;
    TreeNode TreeNode;
    int depth;
    int index;
    boolean leaf;
    Map<String, Object> dynamic;
}