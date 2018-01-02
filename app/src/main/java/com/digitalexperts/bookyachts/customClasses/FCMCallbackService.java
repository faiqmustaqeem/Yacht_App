package com.digitalexperts.bookyachts.customClasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.digitalexperts.bookyachts.R;
import com.digitalexperts.bookyachts.activity.DashboardActivity;


public class FCMCallbackService extends FirebaseMessagingService
{
    int notificationCounter;
    @Override
    public void onCreate()
    {
        super.onCreate();

        //notificationCounter = AppController.getInstance().getPrefManger().getNotificationValue("Notification");
        //notificationCounter++;
        //AppController.getInstance().getPrefManger().setNotificationValue("Notification",notificationCounter);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
      // String click_action = remoteMessage.getNotification().getClickAction();
        Intent i = new Intent(getApplication(), DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       // i.putExtra("notification","notification");
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());

        notificationBuilder.setContentTitle("YACHT APP");
       notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSmallIcon(R.drawable.logo);
        notificationBuilder.setContentIntent(pi);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        nm.notify(0,notificationBuilder.build());

    }


}