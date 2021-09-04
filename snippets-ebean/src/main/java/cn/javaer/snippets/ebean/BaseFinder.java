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

    @Override
    public @NotNull List<T> all() {
        final Query<T> query = query();
        whenModifiedOpt.ifPresent(it -> query.orderBy().desc(it));
        whenCreatedOpt.ifPresent(it -> query.orderBy().desc(it));
        return query.findList();
    }

    public List<T> list(PageParam pageParam) {
        final Query<T> query = query();
        whenModifiedOpt.ifPresent(it -> query.orderBy().desc(it));
        whenCreatedOpt.ifPresent(it -> query.orderBy().desc(it));
        return query.setMaxRows(pageParam.getSize())
            .setFirstRow(pageParam.getOffset())
            .findList();
    }

    public int count() {
        return query().findCount();
    }

    public Page<T> paged(PageParam pageParam) {
        final Query<T> query = query();
        whenModifiedOpt.ifPresent(it -> query.orderBy().desc(it));
        whenCreatedOpt.ifPresent(it -> query.orderBy().desc(it));
        final PagedList<T> pagedList = query
            .setMaxRows(pageParam.getSize())
            .setFirstRow(pageParam.getOffset())
            .findPagedList();
        return Page.of(pagedList.getList(), pagedList.getTotalCount());
    }
}