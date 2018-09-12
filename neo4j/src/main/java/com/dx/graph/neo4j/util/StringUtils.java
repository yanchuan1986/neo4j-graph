package com.dx.graph.neo4j.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by guansheng on 16/4/7.
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean containBlank(String... strs) {
        for (String str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(String[] array, String keyword) {
        if (array == null) {
            return false;
        }
        for (String str : array) {
            if (str.equals(keyword)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> splitStr(String str, String sep) {
        List<String> parts = new ArrayList<>();
        if (isNotEmpty(str)) {
            for (String s : str.split(sep)) {
                if (isNotEmpty(s)) {
                    parts.add(s);
                }
            }
        }
        return parts;
    }

    public static List<String> splitStrTrim(String str, String sep) {
        List<String> parts = new ArrayList<>();
        if (isNotEmpty(str)) {
            for (String s : str.split(sep)) {
                String t = s.trim();
                if (isNotEmpty(t)) {
                    parts.add(t);
                }
            }
        }
        return parts;
    }

    /**
     * 判断是否为数值型
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
