package com.jelly.flink.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class ObjectUtils {

    /**
     * 判断对象是否为null
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (obj instanceof String) {
            return StringUtils.isEmpty((String) obj);
        } else {
            return Objects.isNull(obj);
        }
    }



}
