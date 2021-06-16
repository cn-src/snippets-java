package cn.javaer.snippets.jooq;

import cn.javaer.snippets.model.IntValue;
import org.jooq.impl.AbstractConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cn-src
 */
public class IntValueConverter<U extends Enum<U> & IntValue> extends AbstractConverter<Integer, U> {

    private static final long serialVersionUID = -8038553148215700158L;
    private final Map<Integer, U> lookup;

    public IntValueConverter(final Class<U> toType) {
        super(Integer.class, toType);
        final U[] enums = toType.getEnumConstants();
        this.lookup = new HashMap<>(enums.length);
        for (final U e : enums) {
            this.lookup.put(e.value(), e);
        }
    }

    @Override
    public U from(final Integer databaseObject) {
        if (null == databaseObject) {
            return null;
        }
        return this.lookup.get(databaseObject);
    }

    @Override
    public Integer to(final U userObject) {
        if (null == userObject) {
            return null;
        }
        return userObject.value();
    }
}