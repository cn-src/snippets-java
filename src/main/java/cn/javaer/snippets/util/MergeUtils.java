package cn.javaer.snippets.util;

import cn.javaer.snippets.util.function.Function3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 合并转换工具类。
 *
 * @author cn-src
 */
public interface MergeUtils {

    /**
     * 合并 List, 当 props 属于 src 中对象的一个属性时，根据 id 匹配设置属性值。
     *
     * @param src 源 List
     * @param props 属性 List
     * @param srcPropSetter srcPropSetter
     * @param srcPropIdGetter getter
     * @param propIdGetter getter
     * @param <S> 源类型
     * @param <P> 属性类型
     * @param <ID> id 类型
     *
     * @return 源 List
     */
    static <S, P, ID> List<S> mergeProperty(
        final List<S> src, final Function<S, ID> srcPropIdGetter,
        final BiConsumer<S, P> srcPropSetter,
        final List<P> props, final Function<P, ID> propIdGetter) {

        if (src == null || src.isEmpty() || props == null || props.isEmpty()) {
            return src;
        }
        for (final P p : props) {
            final ID pId = propIdGetter.apply(p);
            for (final S s : src) {
                final ID sId = srcPropIdGetter.apply(s);
                if (sId != null && sId.equals(pId)) {
                    srcPropSetter.accept(s, p);
                }
            }
        }
        return src;
    }

    /**
     * 合并 List, 当 props 属于 src 中对象的一个属性时，根据 id 匹配设置属性值。
     *
     * @param src 源 List
     * @param srcPropIdGetter getter
     * @param resultFun resultFun
     * @param props 属性 List
     * @param propIdGetter getter
     * @param <S> 源类型
     * @param <P> 属性类型
     * @param <ID> id 类型
     *
     * @return 源 List
     */
    static <R, S, P, ID> List<R> mergeProperty(
        final List<S> src, final Function<S, ID> srcPropIdGetter,
        final BiFunction<S, P, R> resultFun,
        final List<P> props, final Function<P, ID> propIdGetter) {

        if (src == null || src.isEmpty() || props == null || props.isEmpty()) {
            return Collections.emptyList();
        }
        final List<R> result = new ArrayList<>(src.size());
        for (final P p : props) {
            final ID pId = propIdGetter.apply(p);
            for (final S s : src) {
                final ID sId = srcPropIdGetter.apply(s);
                if (sId != null && sId.equals(pId)) {
                    result.add(resultFun.apply(s, p));
                }
            }
        }
        return result;
    }

    /**
     * 合并 List, 当 props 属于 src 中对象的一个属性时，根据 id 匹配设置属性值。
     *
     * @param src 源 List
     * @param srcPropIdGetter getter
     * @param resultFun resultFun
     * @param props1 属性1 List
     * @param prop1IdGetter getter
     * @param props2 属性2 List
     * @param prop2IdGetter getter
     * @param <S> 源类型
     * @param <P1> 属性类型
     * @param <P2> 属性类型
     * @param <ID> id 类型
     *
     * @return 源 List
     */
    static <R, S, P1, P2, ID> List<R> mergeProperty(
        final List<S> src, final Function<S, ID> srcPropIdGetter,
        final Function3<S, P1, P2, R> resultFun,
        final List<P1> props1, final Function<P1, ID> prop1IdGetter,
        final List<P2> props2, final Function<P2, ID> prop2IdGetter) {

        if (src == null || src.isEmpty()) {
            return Collections.emptyList();
        }
        final List<R> result = new ArrayList<>(src.size());
        for (final S s : src) {
            final ID sId = srcPropIdGetter.apply(s);

            P1 usedP1 = null;
            if (props1 != null) {
                for (final P1 p1 : props1) {
                    final ID pId = prop1IdGetter.apply(p1);
                    if (sId != null && sId.equals(pId)) {
                        usedP1 = p1;
                    }
                }
            }
            P2 usedP2 = null;
            if (props2 != null) {
                for (final P2 p2 : props2) {
                    final ID pId = prop2IdGetter.apply(p2);
                    if (sId != null && sId.equals(pId)) {
                        usedP2 = p2;
                    }
                }
            }
            result.add(resultFun.apply(s, usedP1, usedP2));
        }
        return result;
    }

    /**
     * 合并 List, 当 props 属于 src 中对象的一个属性时，根据 id 匹配设置属性值。
     *
     * @param src 源 List
     * @param props 属性 List
     * @param srcPropSetter setter
     * @param srcPropIdGetter getter
     * @param propIdGetter getter
     * @param <S> 源类型
     * @param <P> 属性类型
     * @param <ID> id 类型
     *
     * @return 源 List
     */
    static <S, P, ID> List<S> mergePropertyList(
        final List<S> src, final List<P> props, final BiConsumer<S, List<P>> srcPropSetter,
        final Function<S, ID[]> srcPropIdGetter, final Function<P, ID> propIdGetter) {

        if (src == null || src.isEmpty() || props == null || props.isEmpty()) {
            return src;
        }
        for (final S s : src) {
            final ID[] ids = srcPropIdGetter.apply(s);
            final List<P> subProps = new ArrayList<>();
            for (final P p : props) {
                final ID pId = propIdGetter.apply(p);
                if (ids != null && Arrays.asList(ids).contains(pId)) {
                    subProps.add(p);
                }
            }
            srcPropSetter.accept(s, subProps);
        }
        return src;
    }

    /**
     * 一对多合并转换，以唯一 Key 为基准，将对象中部分数据合并转换成对象的 Map 属性。
     *
     * 合并以 uKeyGetter 获取的对象进行比较。源对象和目标对象是同一类型。
     *
     * @param srcList 源对象 List
     * @param uKeyGetter 源对象获取唯一 Key 的函数
     * @param mapGetter 对象 Map 属性的读取函数
     * @param mapSetter 对象 Map 属性的写入函数
     * @param keyCreator 对象 Map 属性的 key 创建函数
     * @param valueCreator 对象 Map 属性的 value 创建函数
     * @param <T> 对T象类型
     * @param <UK> 唯一主键的类型
     * @param <MK> 对象 Map 属性的 key 的类型
     * @param <MV> 对象 Map 属性的 value 的类型
     *
     * @return 合并转换之后的对象 List
     */
    static <T, UK, MK, MV> List<T> mergePropertyToMap(final List<T> srcList,
                                                      final Function<T, UK> uKeyGetter,
                                                      final Function<T, Map<MK, MV>> mapGetter,
                                                      final BiConsumer<T, Map<MK, MV>> mapSetter,
                                                      final Function<T, MK> keyCreator,
                                                      final BiFunction<T, MV, MV> valueCreator) {
        return mergePropertyToMap(srcList,
            uKeyGetter, uKeyGetter,
            mapGetter, mapSetter,
            keyCreator,
            valueCreator,
            t -> t);
    }

    /**
     * 一对多合并转换，以唯一 Key 为基准，将源对象中部分数据合并转换成目标对象的 Map 属性。
     *
     * 合并以 sourceUkGetter 和 targetUkGetter 获取的对象进行比较。
     *
     * @param srcList 源对象 List
     * @param sourceUkGetter 源对象获取唯一 Key 的函数
     * @param targetUkGetter 目标对象获取唯一 Key 的函数
     * @param mapGetter 目标对象 Map 属性的读取函数
     * @param mapSetter 目标对象 Map 属性的写入函数
     * @param keyCreator 目标对象 Map 属性的 key 创建函数
     * @param valueCreator 目标对象 Map 属性的 value 创建函数
     * @param rCreator 目标对象的创建函数
     * @param <S> 源对象类型
     * @param <R> 目标对象类型
     * @param <UK> 唯一主键的类型
     * @param <MK> 目标对象 Map 属性的 key 的类型
     * @param <MV> 目标对象 Map 属性的 value 的类型
     *
     * @return 合并转换之后的对象 List
     */
    static <S, R, UK, MK, MV> List<R> mergePropertyToMap(final List<S> srcList,

                                                         final Function<S, UK> sourceUkGetter,
                                                         final Function<R, UK> targetUkGetter,
                                                         final Function<R, Map<MK, MV>> mapGetter,
                                                         final BiConsumer<R, Map<MK, MV>> mapSetter,

                                                         final Function<S, MK> keyCreator,
                                                         final BiFunction<S, MV, MV> valueCreator,
                                                         final Function<S, R> rCreator) {
        final List<R> results = new ArrayList<>();

        for (final S src : srcList) {
            final R r = results.stream()
                .filter(it -> targetUkGetter.apply(it).equals(sourceUkGetter.apply(src)))
                .findFirst()
                .orElseGet(() -> {
                    final R initR = rCreator.apply(src);
                    results.add(initR);
                    return initR;
                });

            Map<MK, MV> map = mapGetter.apply(r);
            if (map == null) {
                final Map<MK, MV> initMap = new HashMap<>();
                mapSetter.accept(r, initMap);
                map = initMap;
            }
            final MK key = keyCreator.apply(src);
            map.put(key, valueCreator.apply(src, map.get(key)));
        }
        return results;
    }
}
