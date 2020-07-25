package com.omak.omakhelpers;

import android.content.Context;
import android.widget.Button;

import com.omak.samplelibrary.R;

/**
 * App_Theme_Utils class used in MDC-111 application.
 */
public class DialogHelpers {

    private Integer positiveButtonBackground = R.color.colorPrimary;
    private Integer positiveButtonColor = R.color.colorAccent;
    private Integer cancelButtonBackground = R.color.colorAccent;
    private Integer cancelButtonColor = R.color.colorPrimary;

    public void setPrimaryButton(Context context, Button button) {
        button.setBackgroundColor(context.getResources().getColor(positiveButtonBackground));
        button.setTextColor(context.getResources().getColor(positiveButtonColor));
    }

    public void setCancelButton(Context context, Button button) {
        button.setBackgroundColor(context.getResources().getColor(cancelButtonBackground));
        button.setTextColor(context.getResources().getColor(cancelButtonColor));
    }

}
