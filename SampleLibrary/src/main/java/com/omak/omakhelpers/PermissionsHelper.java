package com.omak.omakhelpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionsHelper {
    public static final int REQUEST_PERMISSION_MULTIPLE = 0;
    public static final int REQUEST_PERMISSION_SINGLE = 1;
    public EventListener listener;
    private Activity activity;
    private String[] permissions;

    public PermissionsHelper(Activity activity, String[] permissions, EventListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.permissions = permissions;
    }

    public String[] getRequiredPermissionsArray() {
        return this.permissions;
    }

    //intent for open run time permission
    public void openRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_MULTIPLE);
        }
    }

    //intent for open run time permission
    public void openRuntimePermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, REQUEST_PERMISSION_SINGLE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        HelperFunctions.theLogger("Permission", "" + permissions);
        HelperFunctions.theLogger("grantResults", "" + grantResults);

        Integer totalPermissions = permissions.length;
        Integer grantedPermissions = 0;
        ArrayList<String> permissionNotGranted = new ArrayList<String>();

        if (grantResults != null) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    grantedPermissions++;
                } else {
                    permissionNotGranted.add(permissions[i]);
                }
            }
        }
        switch (requestCode) {
            case 1:
                listener.onPermissionsGranted(grantedPermissions, totalPermissions, permissionNotGranted);
                break;
            case 2:
                listener.onPermissionsGrantedSingle(permissions[0]);
                break;
        }

    }

    public interface EventListener {
        void onPermissionsGranted(Integer grantedPermissions, Integer totalPermissions, ArrayList<String> permissionNotGranted);
        void onPermissionsGrantedSingle(String permission);
    }
}