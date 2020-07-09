package cn.javaer.snippets.jooq;

import cn.javaer.snippets.type.Geometry;
import org.jooq.Binding;
import org.jooq.BindingGetResultSetContext;
import org.jooq.BindingGetSQLInputContext;
import org.jooq.BindingGetStatementContext;
import org.jooq.BindingRegisterContext;
import org.jooq.BindingSQLContext;
import org.jooq.BindingSetSQLOutputContext;
import org.jooq.BindingSetStatementContext;
import org.jooq.Converter;

import java.sql.SQLException;

/**
 * @author cn-src
 */
public class PostGISGeometryBinding implements Binding<Object, Geometry> {
    @Override
    public Converter<Object, Geometry> converter() {
        return null;
    }

    @Override
    public void sql(BindingSQLContext<Geometry> ctx) throws SQLException {

    }

    @Override
    public void register(BindingRegisterContext<Geometry> ctx) throws SQLException {

    }

    @Override
    public void set(BindingSetStatementContext<Geometry> ctx) throws SQLException {

    }

    @Override
    public void set(BindingSetSQLOutputContext<Geometry> ctx) throws SQLException {

    }

    @Override
    public void get(BindingGetResultSetContext<Geometry> ctx) throws SQLException {

    }

    @Override
    public void get(BindingGetStatementContext<Geometry> ctx) throws SQLException {

    }

    @Override
    public void get(BindingGetSQLInputContext<Geometry> ctx) throws SQLException {

    }
}
