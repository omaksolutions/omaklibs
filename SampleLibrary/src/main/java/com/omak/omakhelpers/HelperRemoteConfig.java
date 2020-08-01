package com.omak.omakhelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.omak.samplelibrary.R;

public class HelperRemoteConfig {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 1;
    String imgUrl = "";
    Context context;
    String APP_VERSION = "0.0.0";
    EventListener listener;
    Integer defaultConfig;

    public HelperRemoteConfig(Context context, String APP_VERSION) {
        this.context = context;
        this.APP_VERSION = APP_VERSION;
    }

    public long getCacheExpiration() {
        return cacheExpiration;
    }

    public void setCacheExpiration(long cacheExpiration) {
        this.cacheExpiration = cacheExpiration;
    }

    public Integer getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(Integer resourceId) {
        this.defaultConfig = resourceId;
    }

    public String getAPP_VERSION() {
        return APP_VERSION;
    }

    public void setAPP_VERSION(String APP_VERSION) {
        this.APP_VERSION = APP_VERSION;
    }

    private void checkUpdate() {
        // in activity or fragment, according to your fetching strategy
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(cacheExpiration)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(defaultConfig);
        mFirebaseRemoteConfig.activateFetched();

        checkRemote();

        mFirebaseRemoteConfig.fetch().addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    HelperFunctions.toaster(context, "Fetch succeeded.");
                } else {
                    HelperFunctions.toaster(context, "Fetch failed.");
                }
            }
        });
    }

    private void checkRemote() {

        final String current_version = mFirebaseRemoteConfig.getString("current_version");
        Version installedVersion = new Version(APP_VERSION);
        Version currentVersion = new Version(current_version);

        HelperFunctions.theLogger("CurrentVersion", "Firebase: " + current_version);

        if (currentVersion.compareTo(installedVersion) == 1) {
            HelperFunctions.theLogger("Update Available: " + current_version, "New update available");

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("New Version");
            builder.setIcon(R.drawable.logo);
            builder.setMessage("Update app now. Will take only 2 minutes.");

            // Set the positive button
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onUpdateAvailable(current_version);
                }
            });

            // Set the negative button
            builder.setNegativeButton("Udpate", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public interface EventListener {
        void onUpdateAvailable(String newVersion);
    }


}