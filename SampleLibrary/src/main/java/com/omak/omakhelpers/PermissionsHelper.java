package com.omak.omakhelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.omak.samplelibrary.R;

import java.util.ArrayList;
import java.util.EventListener;

public class PermissionsHelper {
    public static final int REQUEST_PERMISSION_MULTIPLE = 0;
    private Activity activity;
    private String[] permissions;
    public EventListener listener;

    public PermissionsHelper(Activity activity, String[] permissions, EventListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.permissions = permissions;
    }

    public interface EventListener {
        Boolean onPermissionsGranted(Integer grantedPermissions, Integer totalPermissions, ArrayList<String> permissionNotGranted);
    }

    public boolean hasPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public String[] getRequiredPermissionsArray() {
        return this.permissions;
    }

    //intent for open run time permission
    private void openRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_MULTIPLE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        HelperFunctions.theLogger("Permission", "" + permissions);
        HelperFunctions.theLogger("grantResults", "" + grantResults);

        Integer totalPermissions = permissions.length;
        Integer grantedPermissions = 0;
        ArrayList<String> permissionNotGranted = new ArrayList<String>();

        switch (requestCode) {
            case 1:
                if (grantResults.length < 1) return;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        grantedPermissions++;
                    } else {
                        permissionNotGranted.add(permissions[i]);
                    }
                }
        }

        listener.onPermissionsGranted(grantedPermissions, totalPermissions, permissionNotGranted);
    }
}