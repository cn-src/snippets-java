package cn.javaer.snippets.jooq.field;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Support;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.CustomField;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONValue;

import java.util.Collections;

/**
 * @author cn-src
 */
public class JsonbField<R extends Record, T> extends CustomField<T> implements TableField<R, T> {
    private static final long serialVersionUID = -2128410511798819756L;
    private final Table<R> table;

    public JsonbField(final String name, final DataType<T> type) {
        this(name, type, null);
    }

    public JsonbField(final String name, final DataType<T> type, final Table<R> table) {
        super(name, type);

        this.table = table;
    }

    @Override
    public void accept(final Context ctx) {
        if (ctx.qualify() && null != this.table) {
            ctx.visit(this.table);
            ctx.sql('.');
        }
        ctx.visit(this.getUnqualifiedName());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Support(SQLDialect.POSTGRES)
    public Condition containsJsonb(final T object) {
        if (object instanceof Field) {
            return this.containsJsonb((Field) object);
        }
        final String val;
        if (object instanceof String) {
            val = (String) object;
        }
        else if (object instanceof JSONB) {
            val = ((JSONB) object).data();
        }
        else {
            val = object == null ? null : JSONValue.toJSONString(object);
        }
        return DSL.condition("{0}::jsonb @> {1}::jsonb", this,
                DSL.val(val, this.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public Condition containsJsonb(final Field<T> json) {
        return DSL.condition("{0}::jsonb @> {1}::jsonb", this,
                json);
    }

    @Support(SQLDialect.POSTGRES)
    public Condition containsJsonb(final String jsonKey, final Object jsonValue) {
        final String json = JSONValue.toJSONString(Collections.singletonMap(jsonKey, jsonValue));
        return DSL.condition("{0}::jsonb @> {1}::jsonb", this,
                DSL.val(json, this.getDataType()));
    }

    @Override
    public Table<R> getTable() {
        return this.table;
    }
}
