package com.smartsense.gifkar.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        DatabaseHelper database = DatabaseHelper.getInstance(context);
//        List<Reminder> reminderList = database.getNotificationList(RemindersType.ACTIVE);
//        database.close();
//        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//
//        for (Reminder reminder : reminderList) {
//            Calendar calendar = DateAndTimeUtil.parseDateAndTime(reminder.getDateAndTime());
//            calendar.set(Calendar.SECOND, 0);
//            AlarmUtil.setAlarm(context, alarmIntent, reminder.getId(), calendar);
//        }
    }
}