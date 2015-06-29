package com.sicco.erp.util;

import com.sicco.erp.service.GetAllNotificationService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
/**
 * TimeAlarm for starting GetNotificationService
 */
public class Utils {
	public static void scheduleNext(Context context){
		Intent intent = new Intent(context, GetAllNotificationService.class);
		long time = SystemClock.elapsedRealtime();
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time + 300 * 1000,
				PendingIntent.getService(context, 0, intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
		
//		Intent notificationOnStatusBar = new Intent(context, HandleNotificationService.class);
//		long notificationOnStatusBarTime = SystemClock.elapsedRealtime();
//		AlarmManager notificationOnStatusBarManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		notificationOnStatusBarManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationOnStatusBarTime + 300 * 1000,
//				PendingIntent.getService(context, 0, notificationOnStatusBar, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
	}
	
	public static void scheduleNextNext(Context context){
		Intent intent = new Intent(context, GetAllNotificationService.class);
		long time = SystemClock.elapsedRealtime();
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time + 3 * 1000,
				PendingIntent.getService(context, 0, intent, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
		
//		Intent notificationOnStatusBar = new Intent(context, HandleNotificationService.class);
//		long notificationOnStatusBarTime = SystemClock.elapsedRealtime();
//		AlarmManager notificationOnStatusBarManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		notificationOnStatusBarManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationOnStatusBarTime + 3 * 1000,
//				PendingIntent.getService(context, 0, notificationOnStatusBar, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
	}
}
