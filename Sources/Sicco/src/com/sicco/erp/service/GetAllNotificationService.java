package com.sicco.erp.service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.model.NotificationModel;

public class GetAllNotificationService extends Service {
	JSONObject json;
	public static String url_get_notification = "http://office.sicco.vn/api/get_notices.php";

	boolean check_Notification_Count = false;
	Cursor cursor;
	NotificationModel notification;
	NotificationDBController db;
	// ArrayList and variable to getCount
	ArrayList<NotificationModel> congVanXuLy_list = new ArrayList<NotificationModel>();
	ArrayList<NotificationModel> congVanCanPhe_list = new ArrayList<NotificationModel>();
	ArrayList<NotificationModel> cacLoai_list = new ArrayList<NotificationModel>();
	ArrayList<NotificationModel> data = new ArrayList<NotificationModel>();
	NotificationModel temp;

	// count
	static int congVanXuLy, congVanCanPhe, cacLoai;

	//
	private String id = "";
	private String notification_type = "";
	private String ten = "";
	private String content = "";
	private String url = "";
	private String msg_type = "";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// start Service
		Log.d("ToanNM", "onstart()");
		new getAllNotification().execute(url_get_notification);

		return START_STICKY;
	}

	class getAllNotification extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			HTTPHandler handler = new HTTPHandler();
			String ret = "";
			try {
				ret = handler.makeHTTPRequest(url_get_notification,
						HTTPHandler.POST);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject json = new JSONObject(result);
					JSONArray rows = json.getJSONArray("row");
					for (int i = 0; i < rows.length(); i++) {
						json = rows.getJSONObject(i);
						// get all data
						id = json.getString("id");
						notification_type = json.getString("notification_type");
						msg_type = json.getString("message_type");
						ten = json.getString("ten");
						content = json.getString("noi_dung");
						url = json.getString("url");

						// add to db
						db = NotificationDBController
								.getInstance(getApplicationContext());
						String sql = "Select * from "
								+ NotificationDBController.TABLE_NAME
								+ " where " + NotificationDBController.ID_COL
								+ " = " + id;
						cursor = db.rawQuery(sql, null);

						if (cursor != null && cursor.getCount() > 0) {

						} else {
							ContentValues values = new ContentValues();
							values.put(NotificationDBController.ID_COL, id);
							// values.put(NotificationDBController.USERNAME_COL,
							// msg_type_id);
							values.put(NotificationDBController.ID_NOTYFI_COL,
									id);
							values.put(NotificationDBController.CONTENT_COL,
									content);
							values.put(
									NotificationDBController.NOTIFI_TYPE_COL,
									notification_type);
							values.put(NotificationDBController.MSG_TYPE_COL,
									msg_type);
							values.put(NotificationDBController.NAME_COL, ten);
							values.put(NotificationDBController.CONTENT_COL,
									content);
							values.put(NotificationDBController.URL_COL, url);
							values.put(
									NotificationDBController.STATE_COL,
									NotificationDBController.NOTIFICATION_STATE_NEW);

							long rowInserted = db.insert(
									NotificationDBController.TABLE_NAME, null,
									values);
							Log.d("ToanNM", "rowInserted is : " + rowInserted);
						}
					}
					if (cursor != null && cursor.getCount() > 0) {
					} else {
						initMessageData();
						origanizeNoti();
						// Runnable r = new Runnable() {
						//
						// @Override
						// public void run() {
						// for (int i = 0; i < data.size(); i++) {
						// // index = i;
						// try {
						// Thread.currentThread();
						// Thread.sleep(10 * 1000);
						//
						// db.checkedNotification(temp, data
						// .get(i).getId());
						// checkMessageType(data.get(i));
						// } catch (InterruptedException e) {
						// e.printStackTrace();
						// }
						//
						// }
						// }
						// };
						// Thread thread = new Thread(r);
						// thread.start();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	void initMessageData() {
		db = NotificationDBController.getInstance(getApplicationContext());
		data = new ArrayList<NotificationModel>();
		cursor = db.query(NotificationDBController.TABLE_NAME, null, null,
				null, null, null, null);
		String sql = "Select * from " + NotificationDBController.TABLE_NAME
				+ " where " + NotificationDBController.STATE_COL + " = \"new\"";
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {

				int id = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(NotificationDBController.ID_NOTYFI_COL));
				String notify_type = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.NOTIFI_TYPE_COL));
				String msg_type = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.MSG_TYPE_COL));
				String name = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.NAME_COL));
				String content = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.CONTENT_COL));
				String url = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.URL_COL));
				String state = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.STATE_COL));
				// add to arraylist
				temp = new NotificationModel(id, notify_type, msg_type, name,
						content, url, state);
				data.add(temp);
				Log.d("ToanNM", "data size after init Data is : " + data.size());
			} while (cursor.moveToNext());
		}
	}

	public void origanizeNoti() {
		sereprateList(data, "congvan", congVanCanPhe_list);
		sereprateList(data, "congviec", congVanXuLy_list);
		sereprateList(data, "lichbieu", cacLoai_list);

	}

	public void sereprateList(ArrayList<NotificationModel> allList, String key,
			ArrayList<NotificationModel> list) {
		if (allList != null) {
			int max = allList.size();
			for (int i = 0; i < max; i++) {
				if (allList.get(i).getNotify_type().equals(key)) {
					list.add(allList.get(i));
				}
			}
			MyNotificationManager myNotificationManager = new MyNotificationManager();
			myNotificationManager.notifyType(getApplicationContext(), list);
			if (key.equals("congvan")) {
				congVanCanPhe = list.size();
			} else if (key.equals("congviec")) {
				congVanXuLy = list.size();
			} else {
				cacLoai = list.size();
			}
		}
	}

	public static int getCongViecCanPheCount() {
		if (congVanXuLy != 0) {
			return congVanXuLy;
		} else
			return 0;
	}

	public static int getCongViecCanXuLyCount() {
		if (congVanCanPhe != 0) {
			return congVanCanPhe;
		} else
			return 0;
	}

	public static int getCacLoaiCount() {
		if (cacLoai != 0) {
			return cacLoai;
		} else
			return 0;
	}
}
