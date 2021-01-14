package cn.javaer.snippets.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.jooq.JSONB;

/**
 * @author cn-src
 */
public class JSONBSerializer extends Serializer<JSONB> {
    public JSONBSerializer() {
        super(false, true);
    }

    @Override
    public void write(final Kryo kryo, final Output output, final JSONB object) {
        output.writeString(object.data());
    }

    @Override
    public JSONB read(final Kryo kryo, final Input input, final Class type) {
        final String data = input.readString();
        if (null == data) {
            return null;
        }
        return JSONB.valueOf(data);
    }
}
