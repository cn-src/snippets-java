package cn.javaer.snippets.test.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author cn-src
 */
class SubFieldsConditionTest {
    @Test
    @DisplayName("测试正常子集")
    void testCheck() {
        final JavaClasses importedClasses = new ClassFileImporter()
            .importClasses(
                cn.javaer.snippets.test.archunit.case1.Pojo.class,
                cn.javaer.snippets.test.archunit.case1.SubPojo.class);
        classes().should(new SubFieldsCondition("demo test"))
            .check(importedClasses);
    }

    @Test
    @DisplayName("测试不是子集")
    void testCheck2() {
        final JavaClasses importedClasses = new ClassFileImporter()
            .importClasses(
                cn.javaer.snippets.test.archunit.case2.Pojo.class,
                cn.javaer.snippets.test.archunit.case2.SubPojo.class);
        assertThatExceptionOfType(AssertionError.class)
            .isThrownBy(() ->
                classes().should(new SubFieldsCondition("demo test")).check(importedClasses)
            );
    }
}
