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

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.omak.omakhelpers.HelperFunctions;
import com.omak.omakhelpers.HelpersTest;
import com.omak.omakhelpers.RealmHelpers.RealmHelpers;
import com.omak.samplelibrary.R;

import java.io.IOException;
import java.net.URL;

public class NotificationHandler extends FirebaseMessagingService {
    public static Bitmap largeImage, bigImage, smallIconImage;
    NotificationManager notificationManager;
    Context context;
    RealmHelpers realmHelpers;
    int nextNotificationId;
    notiData notiData;
    private NotificationChannelHelpers mNotificationUtils;

    public NotificationHandler() {
        this.context = this;
    }

    public NotificationHandler(Context context, notiData notiData) {
        this.notiData = notiData;
        this.context = context;
        showGeneralNotification();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
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
        realmHelpers = new RealmHelpers(this);
        nextNotificationId = realmHelpers.getNotificationId(notiData);
        Intent intent = new Intent();
        String channelId = NotificationChannelHelpers.CHANNEL_ID_GENERAL;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

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
                new NotificationCompat.Builder(this, channelId)
                        .setLargeIcon(largeImage)
                        .setSmallIcon(R.drawable.logo)
                        .setContentText(notiData.getMessage())
                        .setColor(Color.GREEN)
                        .setContentTitle(notiData.getTitle())
                        .setSound(defaultSoundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notiData.getMessage()))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationCompat.setColor(getResources().getColor(R.color.colorWhite));
        }

        switch (notiData.getType()) {
            case "logout":
                // Perform logout action and clear realm and preferences
                //AccessHelpers.actionLogout(context);
                break;
        }

        intent = new Intent(getApplicationContext(), HelpersTest.clazz);
        intent.putExtra("goto", notiData.getGoTo());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationCompat.setContentIntent(pendingIntent).addAction(R.drawable.logo, "" + notiData.getBtn_title(), pendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ToDo: Find out if channels are created by default or after first notification
            mNotificationUtils = new NotificationChannelHelpers(getApplicationContext(), true);
            notificationCompat.setChannelId(notiData.getChannelId());
            mNotificationUtils.getManager().notify(nextNotificationId, notificationCompat.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(nextNotificationId, notificationCompat.build());
        }
    }
}
