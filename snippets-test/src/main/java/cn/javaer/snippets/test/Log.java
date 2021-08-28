package cn.javaer.snippets.test;

import cn.javaer.snippets.jackson.Json;

/**
 * The interface Log.
 *
 * @author cn -src
 */
public interface Log {

    /**
     * logging json.
     *
     * @param obj the obj
     */
    static void json(Object obj) {
        System.out.println(Json.DEFAULT.write(obj));
    }
}