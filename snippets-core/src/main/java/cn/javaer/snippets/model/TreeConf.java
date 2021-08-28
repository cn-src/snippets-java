package cn.javaer.snippets.model;

import lombok.Value;

import java.util.function.Function;

/**
 * @author cn-src
 */
@Value
public class TreeConf<E> {
    private TreeConf(Function<E, String[]> nameFun, Function<E, Long> sortFun,
                     TreeHandler<E> handler, boolean ignoreEmpty) {
        this.nameFun = nameFun;
        this.sortFun = sortFun;
        this.handler = handler;
        this.ignoreEmpty = ignoreEmpty;
    }

    Function<E, String[]> nameFun;

    Function<E, Long> sortFun;

    TreeHandler<E> handler;

    boolean ignoreEmpty;

    public static <E> TreeConf<E> of(Function<E, String[]> nameFun) {
        return new TreeConf<>(nameFun, e -> null, TreeNodeInfo -> {}, true);
    }
}