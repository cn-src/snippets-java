package cn.javaer.snippets.test.archunit.case3;

import cn.javaer.snippets.test.archunit.SubFields;
import lombok.Data;

/**
 * @author cn-src
 */
@Data
@SubFields(Pojo.class)
public class SubPojo {
    private int field1;
}
