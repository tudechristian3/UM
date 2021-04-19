package com.ultramega.shop.util.firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ultramega.shop.R;
import com.ultramega.shop.activity.notification.NotificationActivity;
import com.ultramega.shop.activity.notification.PushNotificationActivity;
import com.ultramega.shop.common.CommonFunctions;
import com.ultramega.shop.pojo.PushNotification;
import com.ultramega.shop.util.UserPreferenceManager;

import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Ban_Lenovo on 8/30/2017.
 */

public class UMShopFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //=================================
        //prompt notification if only login
        //=================================
        if (UserPreferenceManager.getBooleanPreference(this, UserPreferenceManager.KEY_IS_LOGGED_IN)) {
            sendNotification(remoteMessage.getData());
        }
    }

    /****
     * SAMPLE LANG NI
     */
    private void sendNotification(Map<String, String> messageBody) {
        //mDbUtils
        CommonFunctions.hasNewNotification = true;
        //CommonFunctions.hasNewOrderUpdate = true;

        //set new count for notification
        int count = UserPreferenceManager.getIntPreference(this, UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT) + 1;
        //UserPreferenceManager.preferencePutInteger(UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, count);
        UserPreferenceManager.saveIntegerPreference(this, UserPreferenceManager.KEY_NOTIFICATION_BADGE_COUNT, count);

        //map to object
        JsonElement jsonElement = new Gson().toJsonTree(messageBody);
        PushNotification pushNotif = new Gson().fromJson(jsonElement, PushNotification.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, new Intent(this, NotificationActivity.class),
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String id = "my_channel_01";
        CharSequence name = "Ultramega Shopper";
        String description = "Notifications";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            mChannel.setDescription(description);
            mChannel.setShowBadge(true);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int badgeCount = UserPreferenceManager.getIntPreference(this, "BADGE_COUNT");

        badgeCount = badgeCount + 1;

        UserPreferenceManager.saveIntegerPreference(this, "BADGE_COUNT", badgeCount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(mChannel);
        } else {
            ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.drawable.ic_shopper_notification)
                .setContentTitle(pushNotif.getSubject())
                .setContentText(pushNotif.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setNumber(1)
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) System.currentTimeMillis()  /* ID of notification */, notificationBuilder.build());

        Log.d("antonhttp", "pushNotif: " + pushNotif);

        PushNotificationActivity.start(this, pushNotif);
    }
}
