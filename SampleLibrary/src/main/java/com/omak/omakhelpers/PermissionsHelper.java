package com.omak.omakhelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.omak.samplelibrary.R;

import java.util.ArrayList;

public class PermissionsHelper {
    public static final int REQUEST_PERMISSION_MULTIPLE = 0;
    public static final int REQUEST_PERMISSION_SINGLE = 1;
    public EventListener listener;
    private Activity activity;
    private String[] permissions;
    public String[] stringList = {
            "Permissions Required",
            "You have denied some of the required permissions for this action. Please open settings, go to permissions and grant permissions."
    };

    public PermissionsHelper(Activity activity, String[] permissions) {
        this.activity = activity;
        this.permissions = permissions;
    }

    public void setEventListener(EventListener listener) {
        this.listener = listener;
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

        HelperFunctions.theLogger("Permission", "" +  new Gson().toJson(permissions));
        HelperFunctions.theLogger("grantResults", "" + new Gson().toJson(grantResults));

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

        // Bolt out and Toast a message that listener is not initialized.
        if(listener==null) {
            HelperFunctions.toaster(activity, "Please implement listener with setEventListener method.");
            return;
        }

        listener.onPermissionsGranted(grantedPermissions, totalPermissions, permissionNotGranted);
    }

    public void showDialog(String[] stringList) {
        if(stringList==null) {
            stringList = this.stringList;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(stringList[0]);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(stringList[1]);

        // Set the positive button
        builder.setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogProceed();
            }
        });
        // Set the negative button
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface EventListener {
        void onPermissionsGranted(Integer grantedPermissions, Integer totalPermissions, ArrayList<String> permissionNotGranted);
        void onDialogProceed();
    }
}