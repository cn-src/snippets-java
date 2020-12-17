package cn.javaer.snippets.jooq;

import lombok.Builder;
import lombok.Value;
import org.jooq.Field;

/**
 * @author cn-src
 */
@Value
@Builder
public class TableMeta implements FieldsProvider {

    Field<?>[] selectFields;

    Field<?>[] saveFields;

    @Override
    public Field<?>[] selectFields() {
        return this.selectFields;
    }

    @Override
    public Field<?>[] saveFields() {
        return this.saveFields;
    }
}
