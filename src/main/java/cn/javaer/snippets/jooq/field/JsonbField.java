package cn.javaer.snippets.jooq.field;

import org.jooq.Condition;
import org.jooq.Context;
import org.jooq.DataType;
import org.jooq.Field;
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

    public JsonbField(String name, DataType<T> type, Table<R> table) {
        super(name, type);

        this.table = table;
    }

    @Override
    public void accept(Context ctx) {
        if (ctx.qualify()) {
            ctx.visit(this.table);
            ctx.sql('.');
        }
        ctx.visit(this.getUnqualifiedName());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Support(SQLDialect.POSTGRES)
    public Condition containsJson(T object) {
        if (object instanceof Field) {
            return this.containsJson((Field) object);
        }
        String val;
        if (object instanceof String) {
            val = (String) object;
        }
        else {
            val = object == null ? null : JSONValue.toJSONString(object);
        }
        return DSL.condition("{0}::jsonb @> {1}::jsonb", this,
                DSL.val(val, this.getDataType()));
    }

    @Support(SQLDialect.POSTGRES)
    public Condition containsJson(Field<T> json) {
        return DSL.condition("{0}::jsonb @> {1}::jsonb", this,
                json);
    }

    @Support(SQLDialect.POSTGRES)
    public Condition containsJson(String jsonKey, Object jsonValue) {
        String json = JSONValue.toJSONString(Collections.singletonMap(jsonKey, jsonValue));
        return DSL.condition("{0}::jsonb @> {1}::jsonb", this,
                DSL.val(json, this.getDataType()));
    }

    @Override
    public Table<R> getTable() {
        return this.table;
    }
}
