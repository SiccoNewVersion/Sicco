package com.sicco.erp.manager;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.sicco.erp.ApprovalActivity;
import com.sicco.erp.R;
import com.sicco.erp.model.NotificationModel;

public class MyNotificationManager {
	Context mContext;
	public static int CONGVAN_NOTIFICATION_ID = 1;
	public static int CONGVIEC_NOTIFICATION_ID = 2;
	public static int LICHBIEU_NOTIFICATION_ID = 3;
	public static int NOTIFICATION_ID = 0;
	static String congvan = "congvan";
	static String congviec = "congviec";
	static String lichbieu = "lichbieu";
	NotificationModel notificationModel;
	String message = "";
	String contentText = "";
	String name = "";
	String content = "";
	String url = "";
	String noti = "";
	public int noti_count = 0;
	PendingIntent pendInt;
	String tag;
	static int flags = Intent.FLAG_ACTIVITY_NEW_TASK
			| Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP;

	public static void buildNormalNotification(Context context,
			NotificationModel data) {

	}

	public void notifyType(Context context,
			ArrayList<NotificationModel> arrayList) {
		int notification_count = arrayList.size();
		noti_count = notification_count;

		for (int i = 0; i < notification_count; i++) {
			// get data
			int notification_type = arrayList.get(i).getNotify_type();
			String ten = arrayList.get(i).getSoHieuCongVan();
			String noi_dung = arrayList.get(i).getTrichYeu();
			String ngaydenSicco = arrayList.get(i).getNgayDenSicco();

			if (notification_count == 1) {
				if (notification_type == 1) {
					NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_van);
					tag = congvan;
				}
				if (notification_type == 2) {
					NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_viec);
					tag = congviec;
				}
				if (notification_type == 3) {
					NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.lich_bieu);
					tag = lichbieu;
				}
				message = context.getResources().getString(
						R.string.new_noti_mess)
						+ " " + notification_count + " " + noti + "\n";
				contentText = noi_dung;
				notify(context, NOTIFICATION_ID);
			}

			if (notification_count > 1) {
				if (notification_type == 1) {
					NOTIFICATION_ID = CONGVAN_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_van);
					name += "" + ten + "\n";
					tag = congvan;
				}
				if (notification_type == 2) {
					NOTIFICATION_ID = CONGVIEC_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.cong_viec);
					name += "" + ten + "\n";
					tag = congviec;
				}
				if (notification_type == 3) {
					NOTIFICATION_ID = LICHBIEU_NOTIFICATION_ID;
					noti = context.getResources().getString(R.string.lich_bieu);
					name += "" + ten + "\n";
					tag = lichbieu;
				}
				message = context.getResources().getString(
						R.string.new_noti_mess)
						+ " " + notification_count + " " + noti + " " + "\n";
				contentText = name;
				notify(context, NOTIFICATION_ID);
			}
		}
	}

	// ==========================================================================
	public void notify(Context context, int notification_id) {

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		Intent notIntent = new Intent(context, ApprovalActivity.class);
		notIntent.addFlags(flags);
		// notIntent.putExtra("com.sicco.erp", 1);
		pendInt = PendingIntent.getActivity(context, 0, notIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(pendInt).setOngoing(false).setAutoCancel(true);
		NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
		style.bigText(contentText);
//		style.setSummaryText("Swipe Left or Right to dismiss this Notification.");
		style.build();

		// ==============================

		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(message);
		builder.setContentText(contentText);
		builder.setStyle(style);

		// mo rong
		long[] pattern = { (long) 100, (long) 100, (long) 100, (long) 100,
				(long) 100 };
		builder.setVibrate(pattern);
		builder.setLights(0xFFFFFFFF, 500, 500);
		Notification notification = builder.getNotification();
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(notification_id);
		manager.notify(notification_id, notification);

	}

}
