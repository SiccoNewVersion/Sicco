package com.sicco.erp.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.util.Utils;

public class GetAllNotificationService extends Service {
	JSONObject json;
	String url_get_notification = "";
	Cursor cursor;
	NotificationModel notification;
	NotificationDBController db;
	// ArrayList and variable to getCount
	ArrayList<NotificationModel> congVanXuLy_list;
	ArrayList<NotificationModel> congVanCanPhe_list;
	ArrayList<NotificationModel> cacLoai_list;
	NotificationModel temp;

	// count
	static String CONGVIEC = "congviec", CONGVAN = "congvan",
			LICHBIEU = "lichbieu";
	//
	long id;
	String soHieuCongVan = "";
	String trichYeu = "";
	String dinhKem = "";
	String ngayDenSicco = "";
	String trangThai = "";
	int total;

	Handler handler;
	Runnable runnable;

	// key
	public static String CVCP_KEY = "CONGVIECCANPHE_KEY";
	public static String CVXL_KEY = "CONGVIECXULY_KEY";
	public static String CL_KEY = "CACLOAI_KEY";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// start Service
		Log.d("ToanNM", "GetAllNotification.onstart()");

		CongVanCanPheAsync();
		CongVanXuLyAsync();
		CacLoaiAsync();
		Utils.scheduleNext(getApplicationContext());

		// final int delay = 30 * 10 *1000;
		// handler = new Handler();
		// runnable = new Runnable(){
		// @Override
		// public void run() {
		// CongVanCanPheAsync();
		// CongVanXuLyAsync();
		// CacLoaiAsync();
		// handler.postDelayed(runnable, delay);
		// }
		// };
		// runnable.run();

		return START_STICKY;
	}

	void CongVanCanPheAsync() {
		AsyncHttpClient handler = new AsyncHttpClient();
		url_get_notification = getResources().getString(
				R.string.api_get_dispatch_approval);

		congVanCanPhe_list = new ArrayList<NotificationModel>();

		RequestParams params = new RequestParams();
		String username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
		params.add("username", username);

		handler.post(getApplicationContext(), url_get_notification, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String st = response.toString();
						// Log.d("ToanNM", "json:" + st);

						try {
							JSONObject json = new JSONObject(st);
							total = json.getInt("total");
							Log.d("TuNT", "total 1: "+total);
							JSONArray rows = json.getJSONArray("row");
							for (int i = 0; i < rows.length(); i++) {
								JSONObject row = rows.getJSONObject(i);
								// get all data
								long id = row.getInt("id");
								String soHieuCongVan = row.getString("so_hieu");
								String trichYeu = row.getString("trich_yeu");
								String dinhKem = row.getString("content");
								String ngayDenSicco = row.getString("ngay_den");
								String trangThai = row.getString("status");

								congVanCanPhe_list.add(new NotificationModel(i,
										1, soHieuCongVan, trichYeu, dinhKem,
										ngayDenSicco, trangThai));
							}
							Log.d("ToanNM", "row : " + rows.length()
									+ " , congVanCanPhe_list.size(): "
									+ congVanCanPhe_list.size());
							origanizeNoti(congVanCanPhe_list);
							saveInt(1, total);
						} catch (Exception e) {
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

				});

	}

	void CongVanXuLyAsync() {
		AsyncHttpClient handler = new AsyncHttpClient();
		url_get_notification = getResources().getString(
				R.string.api_get_dispatch_handle);

		congVanXuLy_list = new ArrayList<NotificationModel>();

		RequestParams params = new RequestParams();
		String username = Utils.getString(getApplicationContext(), SessionManager.KEY_NAME);
		params.add("username", username);

		handler.post(getApplicationContext(), url_get_notification, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String st = response.toString();
						// Log.d("ToanNM", "json:" + st);

						try {
							JSONObject json = new JSONObject(st);
							total = json.getInt("total");
							Log.d("TuNT", "total 2: "+total);
							JSONArray rows = json.getJSONArray("row");
							for (int i = 0; i < rows.length(); i++) {
								JSONObject row = rows.getJSONObject(i);
								// get all data
								long id = row.getInt("id");
								String soHieuCongVan = row.getString("so_hieu");
								String trichYeu = row.getString("trich_yeu");
								String dinhKem = row.getString("content");
								String ngayDenSicco = row.getString("ngay_den");
								String trangThai = row.getString("status");

								congVanXuLy_list.add(new NotificationModel(i,
										2, soHieuCongVan, trichYeu, dinhKem,
										ngayDenSicco, trangThai));

							}
							Log.d("ToanNM", "row : " + rows.length()
									+ " , congVanXuLy_list.size(): "
									+ congVanXuLy_list.size());
							origanizeNoti(congVanXuLy_list);
							saveInt(2, total);

						} catch (Exception e) {
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

				});

	}

	void CacLoaiAsync() {
		AsyncHttpClient handler = new AsyncHttpClient();
		url_get_notification = getResources().getString(
				R.string.api_get_dispatch_other);

		RequestParams params = new RequestParams();
		final String username = Utils.getString(getApplicationContext(),
				SessionManager.KEY_NAME);
		params.add("username", username);

		handler.post(getApplicationContext(), url_get_notification, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						String st = response.toString();
						Log.d("ToanNM", "json:" + st);

						try {
							JSONObject json = new JSONObject(st);
							JSONArray rows = json.getJSONArray("row");
							for (int i = 0; i < rows.length(); i++) {
								JSONObject row = rows.getJSONObject(i);
								// get all data
								long id = row.getInt("id");
								String soHieuCongVan = row.getString("so_hieu");
								String trichYeu = row.getString("trich_yeu");
								String dinhKem = row.getString("content");
								String ngayDenSicco = row.getString("ngay_den");
								String trangThai = row.getString("status");

								// // add to db
								db = NotificationDBController
										.getInstance(getApplicationContext());
								String sql = "Select * from "
										+ NotificationDBController.TABLE_NAME
										+ " where "
										+ NotificationDBController.ID_COL
										+ " = " + id;
								cursor = db.rawQuery(sql, null);

								ContentValues values = new ContentValues();
								values.put(NotificationDBController.ID_COL, id);
								// values.put(NotificationDBController.USERNAME_COL,
								// msg_type_id);
								values.put(
										NotificationDBController.NOTIFI_TYPE_COL,
										3);
								values.put(
										NotificationDBController.USERNAME_COL,
										username);
								values.put(
										NotificationDBController.SOHIEUCONGVAN_COL,
										soHieuCongVan);
								values.put(
										NotificationDBController.TRICHYEU_COL,
										trichYeu);
								values.put(
										NotificationDBController.DINHKEM_COL,
										dinhKem);
								values.put(
										NotificationDBController.NGAYDENSICCO_COL,
										ngayDenSicco);
								values.put(
										NotificationDBController.TRANGTHAI_COL,
										NotificationDBController.NOTIFICATION_STATE_NEW);

								long rowInserted = db.insert(
										NotificationDBController.TABLE_NAME,
										null, values);

								Log.d("ToanNM", "rowInserted is : "
										+ rowInserted);
							}
							initMessageData(cacLoai_list, 3);
							if (cursor != null && cursor.getCount() > 0) {
							} else {
								initMessageData(cacLoai_list, 3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

				});

	}

	void initMessageData(ArrayList<NotificationModel> data, int type) {
		db = NotificationDBController.getInstance(getApplicationContext());
		data = new ArrayList<NotificationModel>();
		cursor = db.query(NotificationDBController.TABLE_NAME, null, null,
				null, null, null, null);
		String sql = "Select * from " + NotificationDBController.TABLE_NAME
				+ " where " + NotificationDBController.TRANGTHAI_COL
				+ " = \"new\"" + " and "
				+ NotificationDBController.NOTIFI_TYPE_COL + " = " + type;
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor
						.getInt(cursor
								.getColumnIndexOrThrow(NotificationDBController.ID_COL));
				String soHieuCongVan = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.SOHIEUCONGVAN_COL));
				String trichYeu = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TRICHYEU_COL));
				String dinhKem = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.DINHKEM_COL));
				String ngayDenSicco = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.NGAYDENSICCO_COL));
				String trangThai = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TRANGTHAI_COL));
				// add to arraylist
				temp = new NotificationModel(id, type, soHieuCongVan, trichYeu,
						dinhKem, ngayDenSicco, trangThai);
				data.add(temp);
				Log.d("ToanNM", "data size after init Data is : " + data.size());
			} while (cursor.moveToNext());
		}
		origanizeNoti(data);
		saveInt(type, data.size());
	}

	void saveInt(int type, int size) {
		if (type == 1) {
			Utils.saveInt(getApplicationContext(), CVCP_KEY, size);
		} else if (type == 2) {
			Utils.saveInt(getApplicationContext(), CVXL_KEY, size);
		} else if (type == 3) {
			Utils.saveInt(getApplicationContext(), CL_KEY, size);
		}
	}

	void origanizeNoti(ArrayList<NotificationModel> data) {
		sereprateList(data);
	}

	void sereprateList(ArrayList<NotificationModel> data) {
		MyNotificationManager myNotificationManager = new MyNotificationManager();
		myNotificationManager.notifyType(getApplicationContext(), data);
	}
}