package com.kanmanus.kmutt.sit.ijoint.receiver;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.kanmanus.kmutt.sit.ijoint.R;
import com.kanmanus.kmutt.sit.ijoint.activity.MainActivity;

import java.util.Random;

/**
 * Created by Semicolon07 on 5/11/2016 AD.
 */
public class AlarmIntentService extends IntentService {
    public static  int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public AlarmIntentService(){
        super("AlarmIntentService");
    }

    public AlarmIntentService(String name) {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        notifyMessage();
        AlarmReceiver.completeWakefulIntent(intent);
    }

    private void notifyMessage() {


        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

        Intent intentToLaunch = new Intent(this,MainActivity.class);
        //intentToLaunch = AssignBoxInboxDetailActivity.getCallingIntent(this, Integer.toString(notifyEntity.getAssignNo()));

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intentToLaunch, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = getApplication().getString(R.string.notification_exercise_title);
        String message = getApplication().getString(R.string.notification_exercise_detail);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notifications_active)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        // Vibrate if vibrate is enabled
        Notification notification = mBuilder.build();
        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bell_line_naver);
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL|Notification.FLAG_SHOW_LIGHTS;
        notification.ledARGB = 0xff0000ff;
        notification.ledOnMS = 200;
        notification.ledOffMS = 1500;
        NOTIFICATION_ID = randInt(0,1000);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
