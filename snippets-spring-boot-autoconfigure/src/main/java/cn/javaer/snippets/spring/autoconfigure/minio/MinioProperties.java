package cn.javaer.snippets.spring.autoconfigure.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author cn-src
 */
@Data
@ConfigurationProperties(prefix = "snippets.minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
