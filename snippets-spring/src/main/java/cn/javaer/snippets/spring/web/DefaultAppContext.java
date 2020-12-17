package cn.javaer.snippets.spring.web;

import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author cn-src
 */
public interface DefaultAppContext {
    String REQUEST_ID_PARAM = "requestId";

    /**
     * 设置请求 id.
     */
    static void setRequestId() {
        MDC.put(REQUEST_ID_PARAM, UUID.randomUUID().toString().replaceAll("-", ""));
    }

    /**
     * 回去请求 id.
     *
     * @return the request id
     */
    static String getRequestId() {
        return MDC.get(REQUEST_ID_PARAM);
    }
}
