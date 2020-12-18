package cn.javaer.snippets.jooq;

import lombok.Builder;
import lombok.Value;
import org.jooq.Field;

/**
 * @author cn-src
 */
@Value
@Builder
public class TableMeta implements ColumnsProvider {

    Field<?>[] selectFields;

    Field<?>[] saveFields;

    @Override
    public Field<?>[] selectColumns() {
        return this.selectFields;
    }

    @Override
    public Field<?>[] saveColumns() {
        return this.saveFields;
    }
}
