package com.github.spuchmann.xml.splitter.utils;


import java.util.List;
import java.util.Map;

/**
 * simple util class which covers common functionality.
 *
 * @since 0.2.0
 */
public final class CommonUtils {

    private CommonUtils() {

    }

    public static boolean isNotNullOrEmpty(String value) {
        return value != null ? !value.isEmpty() : false;
    }

    public static boolean isNotNullOrEmpty(List<?> list) {
        return list != null ? !list.isEmpty() : false;
    }

    public static boolean isNotNullOrEmpty(Map<?, String> map) {
        return map != null ? !map.isEmpty() : false;
    }
}
