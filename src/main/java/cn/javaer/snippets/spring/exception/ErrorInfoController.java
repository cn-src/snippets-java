package cn.javaer.snippets.spring.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author cn-src
 */
@RestController
@RequestMapping
public class ErrorInfoController {

    private final ErrorInfoExtractor errorInfoExtractor;

    public ErrorInfoController(final ErrorInfoExtractor errorInfoExtractor) {
        this.errorInfoExtractor = errorInfoExtractor;
    }

    @GetMapping("error_infos")
    public Map<String, DefinedErrorInfo> errorInfos() {
        return this.errorInfoExtractor.getErrorInfos();
    }
}
