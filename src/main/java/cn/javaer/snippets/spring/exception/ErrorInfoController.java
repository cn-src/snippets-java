package cn.javaer.snippets.spring.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author cn-src
 */
@RestController
@RequestMapping
public class ErrorInfoController {

    private final Map<String, DefinedErrorInfo> errorInfos;

    public ErrorInfoController(final ErrorInfoExtractor errorInfoExtractor) {

        final TreeSet<Map.Entry<String, DefinedErrorInfo>> objects =
                new TreeSet<>(Comparator.comparing(it -> it.getValue().getStatus()));
        objects.addAll(errorInfoExtractor.getErrorInfos().entrySet());

        this.errorInfos = new LinkedHashMap<>(objects.size());
        for (final Map.Entry<String, DefinedErrorInfo> entry : objects) {
            this.errorInfos.put(entry.getKey(), entry.getValue());
        }
    }

    @GetMapping("error_infos")
    public Map<String, DefinedErrorInfo> errorInfos() {
        return this.errorInfos;
    }
}
