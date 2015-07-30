package com.sicco.erp.service;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.HomeActivity.NotifyBR;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.util.BadgeUtils;
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
	int action;

	// key
	public static String CVCP_KEY = "CONGVIECCANPHE_KEY";
	public static String CVXL_KEY = "CONGVIECXULY_KEY";
	public static String CL_KEY = "CACLOAI_KEY";
	public static String TOTAL_KEY = "TOTAL_KEY";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressWarnings("null")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// start Service

		CongVanCanPheAsync();
		CongVanXuLyAsync();
		// CacLoaiAsync();
		if (intent != null) {
			action = intent.getIntExtra("ACTION", 0);
		} else {
			try {
				action = intent.getIntExtra("ACTION", 1);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
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
		String username = Utils.getString(getApplicationContext(),
				SessionManager.KEY_NAME);
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
							total = json.getInt("so_cv_can_phe");
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
								String pheduyet = row.getString("phe_duyet");

								if (pheduyet.equals("0")) {
									congVanCanPhe_list
											.add(new NotificationModel(id, 1,
													soHieuCongVan, trichYeu,
													dinhKem, ngayDenSicco,
													trangThai));
								}
							}

							origanizeNoti(congVanCanPhe_list,
									congVanCanPhe_list.size(), 1);
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
		String username = Utils.getString(getApplicationContext(),
				SessionManager.KEY_NAME);
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

								congVanXuLy_list.add(new NotificationModel(id,
										2, soHieuCongVan, trichYeu, dinhKem,
										ngayDenSicco, trangThai));

							}
							origanizeNoti(congVanXuLy_list,
									congVanXuLy_list.size(), 2);
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

		cacLoai_list = new ArrayList<NotificationModel>();

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

						try {
							JSONObject json = new JSONObject(st);
							total = json.getInt("total");
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

								cacLoai_list.add(new NotificationModel(i, 3,
										soHieuCongVan, trichYeu, dinhKem,
										ngayDenSicco, trangThai));

								// // add to db
								db = NotificationDBController
										.getInstance(getApplicationContext());
								String sql = "Select * from "
										+ NotificationDBController.TABLE_NAME
										+ " where "
										+ NotificationDBController.ID_COL
										+ " = "
										+ id
										+ " and "
										+ NotificationDBController.NOTIFI_TYPE_COL
										+ " = " + 3 + " and "
										+ NotificationDBController.USERNAME_COL
										+ " = \"" + username + "\"";
								cursor = db.rawQuery(sql, null);

								if (cursor != null && cursor.getCount() > 0) {

								} else {
									ContentValues values = new ContentValues();
									values.put(NotificationDBController.ID_COL,
											id);
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
											"");
									values.put(
											NotificationDBController.TRANGTHAI_COL,
											NotificationDBController.NOTIFICATION_STATE_NEW);

//									long rowInserted = 
											db
											.insert(NotificationDBController.TABLE_NAME,
													null, values);

								}
							}
							// initMessageData(cacLoai_list, 3, username);
							boolean firstRun = Utils.getBoolean(
									getApplicationContext(), "FIRSTRUN", true);
							if (firstRun) {
								Utils.saveBoolean(getApplicationContext(),
										"FIRSTRUN", false);
								origanizeNoti(cacLoai_list, total, 3);
								saveInt(3, total);
							} else {
								// =================================================
								// \\
								Dispatch dis = new Dispatch(
										getApplicationContext());
								String url = getApplicationContext()
										.getResources()
										.getString(
												R.string.api_get_dispatch_other);
								dData = new ArrayList<Dispatch>();
								dData = dis.getData(getApplicationContext(),
										url, new OnLoadListener() {

											@Override
											public void onSuccess() {
												cacLoai(username);
											}

											@Override
											public void onStart() {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onFalse() {
												// TODO Auto-generated method
												// stub

											}
										}, 1);
							}

							if (cursor != null && cursor.getCount() > 0) {
							} else {
								// initMessageData(cacLoai_list, 3, username);
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

	ArrayList<Dispatch> dData;

	void cacLoai(String username) {
		int count = 0;
		db = NotificationDBController.getInstance(getApplicationContext());
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DSTATE_COL + " = \"new\"" + " and "
				+ NotificationDBController.D_TYPE_COL + " = " + 1 + " and "
				+ NotificationDBController.USERNAME_COL + " = \"" + username
				+ "\"";
		cursor = db.rawQuery(sql, null);
		count = cursor.getCount();
		// ================================================= \\
		// origanizeNoti(cacLoai_list, count);
		sereprateCacLoaiList(dData, count);
		saveInt(3, count);
	}

	void initMessageData(ArrayList<NotificationModel> data, int type,
			String username) {
		db = NotificationDBController.getInstance(getApplicationContext());
		cursor = db.query(NotificationDBController.TABLE_NAME, null, null,
				null, null, null, null);
		String sql = "Select * from " + NotificationDBController.TABLE_NAME
				+ " where " + NotificationDBController.TRANGTHAI_COL
				+ " = \"new\"" + " and "
				+ NotificationDBController.NOTIFI_TYPE_COL + " = " + type
				+ " and " + NotificationDBController.USERNAME_COL + " = \""
				+ username + "\"";
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
			} while (cursor.moveToNext());
		}
		origanizeNoti(data, data.size(), type);

	}

	void saveInt(int type, int size) {
		if (type == 1) {
			Utils.saveInt(getApplicationContext(), CVCP_KEY, size);
		} else if (type == 2) {
			Utils.saveInt(getApplicationContext(), CVXL_KEY, size);
		} else if (type == 3) {
			Utils.saveInt(getApplicationContext(), CL_KEY, size);
		}
		getTotalNotification();
	}
	
	void getTotalNotification(){
		int cvcp_total = Utils.getInt(getApplicationContext(), CVCP_KEY);
		int cvxl_total = Utils.getInt(getApplicationContext(), CVXL_KEY);
		int total = cvcp_total + cvxl_total;
		Utils.saveInt(getApplicationContext(), TOTAL_KEY, total);
		BadgeUtils.setBadge(getApplicationContext(), total);
		
	}

	void origanizeNoti(ArrayList<NotificationModel> data,
			int notification_count, int type) {
		sereprateList(data, notification_count, type);
		NotifyBR notifyBR = new NotifyBR();
		IntentFilter intentFilter = new IntentFilter("acb");
		registerReceiver(notifyBR, intentFilter);
		Intent i = new Intent("acb");
		sendBroadcast(i);
	}

	void sereprateList(ArrayList<NotificationModel> data,
			int notification_count, int type) {
		int size = data.size();
		if (action == 1 && size != 0) {
			MyNotificationManager myNotificationManager = new MyNotificationManager();
			myNotificationManager.notifyType(getApplicationContext(), data,
					notification_count);
		}
		if (size == 0) {
			cancelNotification(getApplicationContext(), type);
			saveInt(type, size);
		}
	}

	void sereprateCacLoaiList(ArrayList<Dispatch> data, int notification_count) {
		int size = data.size();
		if (action == 1 && size != 0) {
			MyNotificationManager myNotificationManager = new MyNotificationManager();
			myNotificationManager.notifyCacLoai(getApplicationContext(), data,
					notification_count);
		}
		if (size == 0) {
			cancelNotification(getApplicationContext(), 3);
			saveInt(3, notification_count);
		}
	}

	void cancelNotification(Context context, int notification_id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(notification_id);
	}

}