package com.omak.omakhelpers;

/**
 * App_Theme_Utils class used in MDC-111 application.
 */
public abstract class StringUtils {

    public static String getTitleFromLabel(String string) {
        string = string.replace("-", " ");
        //string = capitalize(string);
        return string;
    }

}
