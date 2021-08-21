package cn.javaer.snippets.archunit.case1;

import cn.javaer.snippets.archunit.SubFields;
import lombok.Data;

/**
 * @author cn-src
 */
@Data
@SubFields(Pojo.class)
public class SubPojo {
    private String field1;
}