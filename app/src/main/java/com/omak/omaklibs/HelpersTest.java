package com.omak.omaklibs;

import android.content.Context;
import android.widget.Toast;

public class HelpersTest {
    Context context;

    public HelpersTest(Context context) {
        this.context = context;
    }

    public void popMe(String string) {
        Toast.makeText(context, "Toast Message: " + string, Toast.LENGTH_LONG).show();
    }
}
