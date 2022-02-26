package com.example.coalreport.utils;


/**
 * @author a-cper-cpu
 * 判断String是否为空
 */

public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
