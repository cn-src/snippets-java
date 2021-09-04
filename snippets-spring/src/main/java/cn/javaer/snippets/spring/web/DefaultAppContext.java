package cn.javaer.snippets.spring.web;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;

/**
 * @author cn-src
 */
public interface DefaultAppContext {
    String REQUEST_ID_PARAM = "requestId";

    /**
     * 设置请求 id.
     */
    static void setRequestId() {
        MDC.put(REQUEST_ID_PARAM, IdUtil.fastSimpleUUID());
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