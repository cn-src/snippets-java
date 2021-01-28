package cn.javaer.snippets.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 反射相关的工具类.
 *
 * @author cn -src
 */
public interface ReflectionUtils {

    /**
     * 获取字段对应的 Getter 方法名称.
     *
     * @param field the field
     *
     * @return string
     */
    static String toGetterName(final Field field) {
        return boolean.class.equals(field.getType()) ?
            "is" + StrUtils.toFirstCharUpper(field.getName()) :
            "get" + StrUtils.toFirstCharUpper(field.getName());
    }

    /**
     * 获取字段对应的 Getter 方法名称.
     *
     * @param fieldName the field name
     * @param fieldType the field type
     *
     * @return string
     */
    static String toGetterName(final String fieldName,
                               final String fieldType) {
        return boolean.class.getName().equals(fieldType) ?
            "is" + StrUtils.toFirstCharUpper(fieldName) :
            "get" + StrUtils.toFirstCharUpper(fieldName);
    }

    /**
     * 查找字段对应的 Getter 方法.
     *
     * @param clazz the clazz
     * @param field the field
     *
     * @return the method handle
     */
    static Optional<MethodHandle> findGetterByField(final Class<?> clazz, final Field field) {

        try {
            return Optional.of(MethodHandles.lookup()
                .findVirtual(clazz, toGetterName(field), MethodType.methodType(field.getType())));
        }
        catch (final NoSuchMethodException e) {
            return Optional.empty();
        }
        catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 判断元素上是否有指定的注解.
     *
     * @param element 可注解的元素
     * @param annotation 注解
     *
     * @return 有指定的注解返回 true.
     */
    @SuppressWarnings("unchecked")
    static boolean isAnnotated(final AnnotatedElement element, final String annotation) {
        Objects.requireNonNull(element);
        Validate.notEmpty(annotation);

        return getClass(annotation).map(it -> element.getAnnotation((Class<Annotation>) it))
            .isPresent();
    }

    @Nullable
    static <T extends Annotation>
    T getAnnotation(final Class<T> clazz, final Annotation ann) {
        if (clazz.equals(ann.annotationType())) {
            @SuppressWarnings("unchecked")
            final T t = (T) ann;
            return t;
        }
        return ann.annotationType().getAnnotation(clazz);
    }

    @Nullable
    static <T extends Annotation>
    T getAnnotation(final Class<T> clazz, final Annotation... annotations) {
        if (ArrayUtils.isEmpty(annotations)) {
            return null;
        }
        for (final Annotation annotation : annotations) {
            if (annotation == null) {
                continue;
            }
            final T ann = getAnnotation(clazz, annotation);
            if (null != ann) {
                return ann;
            }
        }
        return null;
    }

    @Nullable
    static <T extends Annotation>
    T getAnnotation(final Class<T> clazz, final AnnotatedElement element) {
        final Annotation[] annotations = element.getAnnotations();
        for (final Annotation annotation : annotations) {
            final T ann = getAnnotation(clazz, annotation);
            if (null != ann) {
                return ann;
            }
        }
        return null;
    }

    /**
     * 获取元素上指定注解的属性值.
     *
     * @param element 可注解的元素
     * @param annotation 注解
     * @param attributeName 注解的属性名
     *
     * @return 注解的属性值
     */
    @SuppressWarnings("unchecked")
    static Optional<Object> getAnnotationAttributeValue(final AnnotatedElement element,
                                                        final String annotation,
                                                        final String attributeName) {
        Objects.requireNonNull(element);
        Validate.notEmpty(annotation);
        Validate.notEmpty(attributeName);
        return getClass(annotation)
            .map(it -> element.getAnnotation((Class<Annotation>) it))
            .map(it -> {
                try {
                    return it.annotationType().getField(attributeName).get(it);
                }
                catch (final IllegalAccessException | NoSuchFieldException e) {
                    throw new IllegalStateException(e);
                }
            });
    }

    /**
     * 判断 class 是否存在.
     *
     * @param className the class name
     *
     * @return 返回 true 如果存在
     */
    static boolean isPresent(final String className) {
        Validate.notEmpty(className);
        try {
            Class.forName(className);
            return true;
        }
        catch (final ClassNotFoundException ex) {
            return false;
        }
    }

    /**
     * 获取 class.
     *
     * @param className the class name
     *
     * @return the class
     */
    static Optional<Class<?>> getClass(final String className) {
        Validate.notEmpty(className);
        try {
            return Optional.of(Class.forName(className));
        }
        catch (final ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * 获取继承自父类的泛型类型.
     *
     * @param clazz 需要继承带泛型的父类，并且此类需要泛型实参
     *
     * @return 泛型类型
     */
    static Class<?>[] getSuperclassGenerics(final Class<?> clazz) {
        Objects.requireNonNull(clazz);
        final Type type = clazz.getGenericSuperclass();
        final ParameterizedType pType = (ParameterizedType) type;
        return Arrays.stream(pType.getActualTypeArguments())
            .map(Class.class::cast).toArray(Class[]::new);
    }
}
