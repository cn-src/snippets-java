package cn.javaer.snippets.util;

import cn.javaer.snippets.model.Assemblers;
import cn.javaer.snippets.model.Auditor;
import cn.javaer.snippets.model.Creator;
import cn.javaer.snippets.model.DynamicAssembler;
import cn.javaer.snippets.util.function.Consumer3;
import cn.javaer.snippets.util.function.Function3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * 合并转换工具类。
 *
 * @author cn-src
 */
public interface MergeUtils {

    static <S, R> List<R> merge(final List<S> sList,
                                final BiPredicate<S, R> mergePredicate,
                                final BiConsumer<S, R> handler,
                                final Function<S, R> rCreator) {

        Objects.requireNonNull(mergePredicate);
        Objects.requireNonNull(handler);
        Objects.requireNonNull(rCreator);
        if (sList == null || sList.isEmpty()) {
            return Collections.emptyList();
        }

        final List<R> results = new ArrayList<>();
        for (final S s : sList) {
            final R r = results.stream()
                .filter(it -> mergePredicate.test(s, it))
                .findFirst()
                .orElseGet(() -> {
                    final R initR = rCreator.apply(s);
                    results.add(initR);
                    return initR;
                });
            handler.accept(s, r);
        }
        return results;
    }

    static <S> List<S> merge(final List<S> sList,
                             final BiPredicate<S, S> mergePredicate,
                             final BiConsumer<S, S> handler) {
        return merge(sList, mergePredicate, handler, s -> s);
    }

    static <S, P, R> List<R> merge(final List<S> sList, final List<P> pList,
                                   final BiPredicate<S, P> mergePredicate,
                                   final BiFunction<S, P, R> resultFun) {

        Objects.requireNonNull(mergePredicate);
        Objects.requireNonNull(resultFun);
        if (sList == null || sList.isEmpty()) {
            return Collections.emptyList();
        }

        final List<R> result = new ArrayList<>(sList.size());
        for (final S s : sList) {
            P used = null;
            if (null != pList) {
                for (final P p : pList) {
                    if (mergePredicate.test(s, p)) {
                        used = p;
                        break;
                    }
                }
            }
            result.add(resultFun.apply(s, used));
        }
        return result;
    }

    static <S, P> List<S> merge(final List<S> sList, final List<P> pList,
                                final BiPredicate<S, P> mergePredicate,
                                final BiConsumer<S, P> resultFun) {
        return merge(sList, pList, mergePredicate, (s, p) -> {
            resultFun.accept(s, p);
            return s;
        });
    }

    static <S, P, R> List<R> merge(final List<S> sList, final List<P> pList,
                                   final BiPredicate<S, P> mergePredicate1,
                                   final BiPredicate<S, P> mergePredicate2,
                                   final Function3<S, P, P, R> resultFun) {

        Objects.requireNonNull(mergePredicate1);
        Objects.requireNonNull(mergePredicate2);
        Objects.requireNonNull(resultFun);
        if (sList == null || sList.isEmpty()) {
            return Collections.emptyList();
        }

        final List<R> result = new ArrayList<>(sList.size());
        for (final S s : sList) {
            P p1 = null;
            P p2 = null;
            if (pList != null) {
                for (final P p : pList) {
                    if (mergePredicate1.test(s, p)) {
                        p1 = p;
                    }
                    if (mergePredicate2.test(s, p)) {
                        p2 = p;
                        break;
                    }
                }
            }
            result.add(resultFun.apply(s, p1, p2));
        }
        return result;
    }

    static <S, P> List<S> merge(final List<S> sList, final List<P> pList,
                                final BiPredicate<S, P> mergePredicate1,
                                final BiPredicate<S, P> mergePredicate2,
                                final Consumer3<S, P, P> resultFun) {
        return merge(sList, pList, mergePredicate1, mergePredicate2, (s, p1, p2) -> {
            resultFun.accept(s, p1, p2);
            return s;
        });
    }

    static <R, S, P1, P2> List<R> merge(
        final List<S> sList,
        final List<P1> p1List, final BiPredicate<S, P1> mergePredicate1,
        final List<P2> p2List, final BiPredicate<S, P2> mergePredicate2,
        final Function3<S, P1, P2, R> resultFun) {

        Objects.requireNonNull(mergePredicate1);
        Objects.requireNonNull(mergePredicate2);
        Objects.requireNonNull(resultFun);
        if (sList == null || sList.isEmpty()) {
            return Collections.emptyList();
        }

        final Function<S, P1> getP1;
        if (p1List == null || p1List.isEmpty()) {
            getP1 = (s) -> null;
        }
        else {
            getP1 = (s) -> {
                for (final P1 p : p1List) {
                    if (mergePredicate1.test(s, p)) {
                        return p;
                    }
                }
                return null;
            };
        }

        final Function<S, P2> getP2;
        if (p2List == null || p2List.isEmpty()) {
            getP2 = (s) -> null;
        }
        else {
            getP2 = (s) -> {
                for (final P2 p : p2List) {
                    if (mergePredicate2.test(s, p)) {
                        return p;
                    }
                }
                return null;
            };
        }
        final List<R> result = new ArrayList<>(sList.size());
        for (final S s : sList) {
            result.add(resultFun.apply(s, getP1.apply(s), getP2.apply(s)));
        }
        return result;
    }

    static <S, P1, P2> List<S> merge(
        final List<S> sList,
        final List<P1> p1List, final BiPredicate<S, P1> mergePredicate1,
        final List<P2> p2List, final BiPredicate<S, P2> mergePredicate2,
        final Consumer3<S, P1, P2> resultFun) {
        return merge(sList, p1List, mergePredicate1, p2List, mergePredicate2, (s, p1, p2) -> {
            resultFun.accept(s, p1, p2);
            return s;
        });
    }

    static <S, P> List<DynamicAssembler<S, P>> mergeToAssembler(
        final String propName,
        final List<S> sList, final List<P> pList,
        final BiPredicate<S, P> mergePredicate) {

        return merge(sList, pList, mergePredicate, (s, p) -> {
            return Assemblers.ofDynamic(s, propName, p);
        });
    }

    static <S, P> List<Creator<S, P>> mergeToCreator(
        final List<S> sList, final List<P> pList,
        final BiPredicate<S, P> mergePredicate) {
        return merge(sList, pList, mergePredicate, (BiFunction<S, P, Creator<S, P>>) Creator::of);
    }

    static <S, P> List<Auditor<S, P>> mergeToAuditor(
        final List<S> sList, final List<P> pList,
        final BiPredicate<S, P> mergePredicate1,
        final BiPredicate<S, P> mergePredicate2) {
        return merge(sList, pList, mergePredicate1, mergePredicate2,
            (Function3<S, P, P, Auditor<S, P>>) Auditor::of);
    }
}
