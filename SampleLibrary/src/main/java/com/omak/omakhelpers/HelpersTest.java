package com.omak.omakhelpers;

import android.content.Context;
import android.widget.Toast;

public class HelpersTest {
    public static Class clazz;
    Context context;

    public HelpersTest(Context context) {
        this.context = context;
    }

    public HelpersTest(Context context, Class clazz) {
        this.context = context;
        HelpersTest.clazz = clazz;
    }

    public void popMe(String string) {
        Toast.makeText(context, "Toast Message: " + string, Toast.LENGTH_LONG).show();
    }
}
