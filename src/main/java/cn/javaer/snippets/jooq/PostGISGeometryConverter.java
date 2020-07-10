package cn.javaer.snippets.jooq;

import cn.javaer.snippets.type.Geometry;
import org.jooq.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

/**
 * @author cn-src
 */
public enum PostGISGeometryConverter implements Converter<PGobject, Geometry> {

    /**
     * 单实例
     */
    INSTANCE;

    private static final long serialVersionUID = 599360862926272439L;

    @Override
    public Geometry from(PGobject db) {

        return null == db ? null : Geometry.valueOf(db.getValue());
    }

    @Override
    public PGobject to(Geometry userObject) {
        if (null == userObject) {
            return null;
        }
        PGobject pg = new PGobject();
        try {
            pg.setType("geometry");
            pg.setValue(userObject.data());
        }
        catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return pg;
    }

    @Override
    public Class<PGobject> fromType() {
        return PGobject.class;
    }

    @Override
    public Class<Geometry> toType() {
        return Geometry.class;
    }
}
