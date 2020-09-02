package cn.javaer.snippets.kryo.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryo.util.ObjectIntMap;

import java.beans.ConstructorProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import static com.esotericsoftware.minlog.Log.TRACE;

/**
 * @author cn-src
 */
@SuppressWarnings("rawtypes")
public class ValueObjectSerializer<T> extends FieldSerializer<T> {
    private Constructor constructor;
    private ObjectIntMap<String> paramsIndex;

    public ValueObjectSerializer(final Kryo kryo, final Class<T> type) {
        super(kryo, type);
        final Constructor constructor = findConstructor(type);
        constructor.setAccessible(true);
        this.constructor = constructor;
        this.paramsIndex = paramsIndex(constructor);
    }

    public ValueObjectSerializer(final Kryo kryo, final Class type,
                                 final FieldSerializerConfig config) {
        super(kryo, type, config);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(final Kryo kryo, final Input input, final Class type) {
        final int pop = this.pushTypeVariables();

        final CachedField[] fields = this.getFields();
        final Object[] fieldValues = new Object[fields.length];
        for (final CachedField field : fields) {
            if (TRACE) {
                this.log("Read", field, input.position());
            }
            @SuppressWarnings("unchecked")
            final Object value = kryo.readObjectOrNull(input, field.getValueClass());
            fieldValues[this.paramsIndex.get(field.getName(), -1)] = value;
        }

        if (pop > 0) {
            this.popTypeVariables(pop);
        }
        try {
            return (T) this.constructor.newInstance(fieldValues);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Constructor findConstructor(final Class type) {
        Constructor useConstructor = null;
        final Constructor[] constructors = type.getDeclaredConstructors();
        if (constructors.length == 1) {
            useConstructor = constructors[0];
        }
        else {
            final Constructor[] cs = Arrays.stream(constructors)
                .filter(it -> it.isAnnotationPresent(ConstructorProperties.class))
                .toArray(Constructor[]::new);
            if (cs.length == 1) {
                useConstructor = cs[0];
            }
        }
        if (null == useConstructor) {
            throw new KryoException("Unable to find suitable a Constructor");
        }

        return useConstructor;
    }

    protected static ObjectIntMap<String> paramsIndex(final Constructor constructor) {
        final ConstructorProperties annotation =
            constructor.getDeclaredAnnotation(ConstructorProperties.class);
        final ObjectIntMap<String> result = new ObjectIntMap<>();
        if (annotation != null) {
            for (int i = 0, le = annotation.value().length; i < le; i++) {
                result.put(annotation.value()[i], i);
            }
        }
        else {
            final Parameter[] parameters = constructor.getParameters();
            for (int i = 0, le = parameters.length; i < le; i++) {
                result.put(parameters[i].getName(), i);
            }
        }
        return result;
    }
}
