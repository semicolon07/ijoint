package com.kanmanus.kmutt.sit.ijoint.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kanmanus.kmutt.sit.ijoint.activity.ApplicationSettingActivity;
import com.kanmanus.kmutt.sit.ijoint.datamanager.MyAlarmManager;
import com.kanmanus.kmutt.sit.ijoint.view.TimePreference;

/**
 * Created by Semicolon07 on 5/12/2016 AD.
 */
public class StartUpBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("StartUpBootReceiver", "ACTION_BOOT_COMPLETED");

            String value = PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getString(ApplicationSettingActivity.KEY_PREF_TIME,"");

            String time = value;

            int hour = TimePreference.getHour(time);
            int minute = TimePreference.getMinute(time);
            MyAlarmManager.getInstance().setExerciseNotificationTime(hour,minute,"");
            Log.d("StartUpBootReceiver", "Set alarm time = "+TimePreference.time24HourFormat(time));
        }
    }
}
