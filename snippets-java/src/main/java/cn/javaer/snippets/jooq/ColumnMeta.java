package cn.javaer.snippets.jooq;

import lombok.Builder;
import lombok.Value;
import org.jooq.Field;
import org.jooq.impl.DSL;

@Value
@Builder
public class ColumnMeta {

    String field;
    String getterName;
    Field<?> column;
    boolean id;
    boolean creator;
    boolean createdDate;
    boolean updater;
    boolean updateDate;

    public static class ColumnMetaBuilder {
        public ColumnMetaBuilder column(final String column) {
            this.column = DSL.field(column);
            return this;
        }
    }
}