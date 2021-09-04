package cn.javaer.snippets.ebean;

import cn.javaer.snippets.model.Page;
import cn.javaer.snippets.model.PageParam;
import cn.javaer.snippets.util.ReflectionUtils;
import io.ebean.Finder;
import io.ebean.PagedList;
import io.ebean.Query;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * @author cn-src
 */
public class BaseFinder<I, T> extends Finder<I, T> {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> whenCreatedOpt;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<String> whenModifiedOpt;

    public BaseFinder(Class<T> type) {
        this(type, null);
    }

    public BaseFinder(Class<T> type, String databaseName) {
        super(type, databaseName);
        this.whenCreatedOpt = ReflectionUtils.fieldNameByAnnotation(type, WhenCreated.class);
        this.whenModifiedOpt = ReflectionUtils.fieldNameByAnnotation(type, WhenModified.class);
    }

    public Query<T> sortQuery() {
        final Query<T> query = query();
        whenModifiedOpt.ifPresent(it -> query.orderBy().desc(it));
        whenCreatedOpt.ifPresent(it -> query.orderBy().desc(it));
        return query;
    }

    public Query<T> pageQuery(PageParam pageParam) {
        return query()
            .setMaxRows(pageParam.getSize())
            .setFirstRow(pageParam.getOffset());
    }

    public Query<T> pageSortQuery(PageParam pageParam) {
        final Query<T> query = query()
            .setMaxRows(pageParam.getSize())
            .setFirstRow(pageParam.getOffset());
        whenModifiedOpt.ifPresent(it -> query.orderBy().desc(it));
        whenCreatedOpt.ifPresent(it -> query.orderBy().desc(it));
        return query;
    }

    public @NotNull List<T> allSort() {
        return sortQuery().findList();
    }

    public List<T> list(PageParam pageParam) {
        return query().setMaxRows(pageParam.getSize())
            .setFirstRow(pageParam.getOffset())
            .findList();
    }

    public List<T> listSort(PageParam pageParam) {
        return sortQuery().setMaxRows(pageParam.getSize())
            .setFirstRow(pageParam.getOffset())
            .findList();
    }

    public int count() {
        return query().findCount();
    }

    public Page<T> paged(PageParam pageParam) {
        final PagedList<T> pagedList = pageQuery(pageParam).findPagedList();
        return Page.of(pagedList.getList(), pagedList.getTotalCount());
    }

    public Page<T> pagedSort(PageParam pageParam) {
        final PagedList<T> pagedList = pageSortQuery(pageParam).findPagedList();
        return Page.of(pagedList.getList(), pagedList.getTotalCount());
    }
}