package cn.javaer.snippets.test.archunit;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 检测类的字段是否是另一个类字段的子集.
 *
 * @author cn-src
 */
public class SubFieldsCondition extends ArchCondition<JavaClass> {

    public static final String TRANSIENT = "org.springframework.data.annotation.Transient";
    public static final String PERSISTENT_FIELDS =
        "cn.javaer.snippets.test.archunit.SubFields";

    private Iterable<JavaClass> allObjectsToTest;

    public SubFieldsCondition(final String description, final Object... args) {
        super(description, args);
    }

    @Override
    public void init(final Iterable<JavaClass> allObjectsToTest) {
        this.allObjectsToTest = allObjectsToTest;
    }

    @Override
    public void check(final JavaClass projectionClass, final ConditionEvents events) {
        if (!projectionClass.isAnnotatedWith(PERSISTENT_FIELDS)) {
            return;
        }
        final JavaAnnotation<JavaClass> sunAnn =
            projectionClass.getAnnotationOfType(PERSISTENT_FIELDS);

        final String entityClassName = sunAnn.get("value")
            .transform(JavaClass.class::cast)
            .transform(JavaClass::getName).orNull();
        final Set<EqualField> projectionFields = projectionClass.getAllFields().stream()
            .filter(it -> !it.getModifiers().contains(JavaModifier.STATIC))
            .filter(it -> !it.isAnnotatedWith(TRANSIENT))
            .map(it -> new EqualField(it.getName(), it.getRawType().getName()))
            .collect(Collectors.toSet());

        boolean isOk = false;
        for (final JavaClass entityClass : this.allObjectsToTest) {
            if (entityClass.getName().equals(entityClassName)) {
                final Set<EqualField> entityFields = entityClass.getAllFields().stream()
                    .filter(it -> !it.getModifiers().contains(JavaModifier.STATIC))
                    .filter(it -> !it.isAnnotatedWith(TRANSIENT))
                    .map(it -> new EqualField(it.getName(), it.getRawType().getName()))
                    .collect(Collectors.toSet());
                if (entityFields.containsAll(projectionFields)) {
                    isOk = true;
                    break;
                }
            }
        }
        if (!isOk) {
            final String message = String.format(
                "The fields of class %s are not a subset of the fields of class %s",
                projectionClass.getName(), entityClassName);
            events.add(SimpleConditionEvent.violated(projectionClass, message));
        }
    }
}
