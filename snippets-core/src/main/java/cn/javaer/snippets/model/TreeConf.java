package cn.javaer.snippets.model;

import cn.hutool.core.util.ObjectUtil;
import cn.javaer.snippets.util.Empty;
import lombok.Builder;
import lombok.Value;

import java.util.function.Function;

/**
 * @author cn-src
 */
@Value
@Builder
public class TreeConf<E> {

    private TreeConf(Function<E, String[]> nameFun, Function<E, Long> sortFun,
                     TreeHandler<E> handler, boolean ignoreEmpty) {
        this.nameFun = ObjectUtil.defaultIfNull(nameFun, Empty.function());
        this.sortFun = ObjectUtil.defaultIfNull(sortFun, Empty.function());
        this.handler = ObjectUtil.defaultIfNull(handler, TreeHandler.empty());
        this.ignoreEmpty = ignoreEmpty;
    }

    Function<E, String[]> nameFun;

    Function<E, Long> sortFun;

    TreeHandler<E> handler;

    boolean ignoreEmpty;

    public static <E> TreeConf<E> of(Function<E, String[]> nameFun) {
        return new TreeConf<>(nameFun, Empty.function(), TreeHandler.empty(), true);
    }
}