package com.omak.omakhelpers;

import java.util.ArrayList;
import java.util.List;

/**
 * App_Theme_Utils class used in MDC-111 application.
 */
public abstract class StringUtils {

    public static String getTitleFromLabel(String string) {
        string = string.replace("-", " ");
        string = string.replace("_", " ");
        //string = capitalize(string);
        return string;
    }

    public static List<String> splitEqually(final String text, final int size) {

        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            if (start + size > 0) {

                String temp = text.substring(start, Math.min(text.length(), start + size));
                int length = temp.length();

                for (int i = 0; i < (size - length); i++) {
                    temp = temp + " ";
                }

                ret.add(temp);

            } else {
                ret.add(text.substring(start, Math.min(text.length(), start + size)));
            }
        }
        return ret;
    }

}
