package com.acmtc.redisMQ.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {

    private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

    public StringUtil() {
    }

    /**
     * 是否空串
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (s == null || "".equals(s.trim())) {
            return true;
        }
        return false;
    }
}
