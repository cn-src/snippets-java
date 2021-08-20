package cn.javaer.snippets.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanWrapperImpl;

/**
 * @author cn-src
 */

public class AllOrNoneEmptyValidator implements ConstraintValidator<AllOrNoneEmpty, Object> {

    private String[] fields;

    @Override
    public void initialize(AllOrNoneEmpty constraintAnnotation) {
        this.fields = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (ArrayUtils.isEmpty(fields)) {
            return true;
        }

        final BeanWrapperImpl bw = new BeanWrapperImpl(value);
        int i = 0;
        for (String field : fields) {
            if (ObjectUtils.isEmpty(bw.getPropertyValue(field))) {
                i++;
            }
        }
        return i == 0 || fields.length == i;
    }
}