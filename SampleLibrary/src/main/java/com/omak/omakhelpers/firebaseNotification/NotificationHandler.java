package com.omak.omakhelpers.firebaseNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.omak.omakhelpers.HelperFunctions;
import com.omak.omakhelpers.RealmHelpers.RealmHelpers;
import com.omak.samplelibrary.R;

import java.io.IOException;
import java.net.URL;

public class NotificationHandler {
    public static Bitmap largeImage, bigImage, smallIconImage;
    NotificationManager notificationManager;
    Context context;
    RealmHelpers realmHelpers;
    int nextNotificationId;
    notiData notiData;
    Class mainClass;
    EventListener listener;
    Integer notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
    private NotificationChannelHelpers mNotificationUtils;

    public NotificationHandler(Context context, Class clazz) {
        this.context = context;
        this.mainClass = clazz;
    }

    public NotificationHandler(Context context, Class clazz, EventListener listener) {
        this.context = context;
        this.mainClass = clazz;
        this.listener = listener;
    }

    public NotificationHandler(Context context, notiData notiData) {
        this.notiData = notiData;
        this.context = context;
        showGeneralNotification();
    }

    public void setPriority(Integer priority) {
        notificationPriority = priority;
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    public void onMessageReceived(RemoteMessage remoteMessage) {
        HelperFunctions.theLogger("Check Notification", "" + new Gson().toJson(remoteMessage));

        notiData = new notiData(remoteMessage);
        showGeneralNotification();
    }

    private String getDataKey(RemoteMessage remoteMessage, String key) {
        return (remoteMessage.getData().get(key) != null) ? remoteMessage.getData().get(key) : "";
    }

    public void clearNotification(int id) {
        //  NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);// o is the notification id which we want to remove after some actions
    }

    private void showGeneralNotification() {
        realmHelpers = new RealmHelpers(context);
        nextNotificationId = realmHelpers.getNotificationId(notiData);
        Intent intent = new Intent();
        String channelId = NotificationChannelHelpers.CHANNEL_ID_GENERAL;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Listener to interact with notiData or do operations on it
        notiData = listener.onNotificationReceived(notiData);

        try {
            URL largeImage = new URL(notiData.getLongImageUrl());
            URL bigImages = new URL(notiData.getSmallImageUrl());
            URL smallUrlicon = new URL(notiData.getSmallIconUrl());

            NotificationHandler.largeImage = BitmapFactory.decodeStream(largeImage.openConnection().getInputStream());
            bigImage = BitmapFactory.decodeStream(bigImages.openConnection().getInputStream());
            smallIconImage = BitmapFactory.decodeStream(smallUrlicon.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }

        NotificationCompat.Builder notificationCompat =
                new NotificationCompat.Builder(context, channelId)
                        .setLargeIcon(largeImage)
                        .setSmallIcon(R.drawable.logo)
                        .setContentText(notiData.getMessage())
                        .setColor(Color.GREEN)
                        .setContentTitle(notiData.getTitle())
                        .setOngoing(notiData.getOngoing())
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notiData.getMessage()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationCompat.setColor(context.getResources().getColor(R.color.colorWhite));
        }


        switch (notiData.getType()) {
            case "logout":
                // Perform logout action and clear realm and preferences
                //AccessHelpers.actionLogout(context);
                break;
        }

        intent = new Intent(context, mainClass);
        intent.putExtra("goto", notiData.getGoTo());
        intent.putExtra("type", notiData.getType());
        intent.putExtra("notiData", notiData);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompat.setContentIntent(pendingIntent).addAction(R.drawable.logo, "" + notiData.getBtn_title(), pendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ToDo: Find out if channels are created by default or after first notification
            mNotificationUtils = new NotificationChannelHelpers(context, true);
            notificationCompat.setChannelId(notiData.getChannelId());
            mNotificationUtils.getManager().notify(nextNotificationId, notificationCompat.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(nextNotificationId, notificationCompat.build());
        }
    }

    public interface EventListener {
        notiData onNotificationReceived(notiData notiData);
    }
}