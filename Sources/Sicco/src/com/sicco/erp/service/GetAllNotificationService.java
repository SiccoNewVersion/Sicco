package com.sicco.erp.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.HomeActivity.NotifyBR;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.NotificationModel;
import com.sicco.erp.util.BadgeUtils;
import com.sicco.erp.util.Utils;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.Task;
import com.sicco.task.model.Task.OnLoadListener;

public class GetAllNotificationService extends Service {
	JSONObject json;
	String url_get_notification = "", url_get_congviec = "",
			url_get_binhluan = "";
	Cursor cursor;
	NotificationModel notification;
	NotificationDBController db;
	// ArrayList and variable to getCount
	ArrayList<NotificationModel> congVanXuLy_list;
	ArrayList<NotificationModel> congVanCanPhe_list;
	ArrayList<NotificationModel> cacLoai_list;
	NotificationModel temp;

	ArrayList<Task> taskData;
	ArrayList<ReportSteerTask> reportData;
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
	NotifyBR notifyBR;
	String username = "";
	boolean inserted = false;
	boolean insertedTask = false;
	String cv_id = "";

	// key
	public static String CVCP_KEY = "CONGVIECCANPHE_KEY";
	public static String CVXL_KEY = "CONGVIECXULY_KEY";
	public static String CL_KEY = "CACLOAI_KEY";
	public static String CV_KEY = "CONGVIEC_KEY";
	public static String BL_ID = "BL_ID";
	public static String TOTAL_KEY = "TOTAL_KEY";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(notifyBR);
	}

	@SuppressWarnings("null")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// start Service

		CongVanCanPheAsync();
		CongVanXuLyAsync();
		// CacLoaiAsync();
		CongViecAsync();
		BinhLuanAsync();
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

								if (trangThai.equals("2")) {
									congVanXuLy_list.add(new NotificationModel(
											id, 2, soHieuCongVan, trichYeu,
											dinhKem, ngayDenSicco, trangThai));
								}

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

	// void CacLoaiAsync() {
	// AsyncHttpClient handler = new AsyncHttpClient();
	// url_get_notification = getResources().getString(
	// R.string.api_get_dispatch_other);
	//
	// cacLoai_list = new ArrayList<NotificationModel>();
	//
	// RequestParams params = new RequestParams();
	// final String username = Utils.getString(getApplicationContext(),
	// SessionManager.KEY_NAME);
	// params.add("username", username);
	//
	// handler.post(getApplicationContext(), url_get_notification, params,
	// new JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(int statusCode, Header[] headers,
	// JSONObject response) {
	// String st = response.toString();
	//
	// try {
	// JSONObject json = new JSONObject(st);
	// total = json.getInt("total");
	// JSONArray rows = json.getJSONArray("row");
	// for (int i = 0; i < rows.length(); i++) {
	// JSONObject row = rows.getJSONObject(i);
	// // get all data
	// long id = row.getInt("id");
	// String soHieuCongVan = row.getString("so_hieu");
	// String trichYeu = row.getString("trich_yeu");
	// String dinhKem = row.getString("content");
	// String ngayDenSicco = row.getString("ngay_den");
	// String trangThai = row.getString("status");
	//
	// cacLoai_list.add(new NotificationModel(i, 3,
	// soHieuCongVan, trichYeu, dinhKem,
	// ngayDenSicco, trangThai));
	//
	// // // add to db
	// db = NotificationDBController
	// .getInstance(getApplicationContext());
	// String sql = "Select * from "
	// + NotificationDBController.TABLE_NAME
	// + " where "
	// + NotificationDBController.ID_COL
	// + " = "
	// + id
	// + " and "
	// + NotificationDBController.NOTIFI_TYPE_COL
	// + " = " + 3 + " and "
	// + NotificationDBController.USERNAME_COL
	// + " = \"" + username + "\"";
	// cursor = db.rawQuery(sql, null);
	//
	// if (cursor != null && cursor.getCount() > 0) {
	//
	// } else {
	// ContentValues values = new ContentValues();
	// values.put(NotificationDBController.ID_COL,
	// id);
	// values.put(
	// NotificationDBController.NOTIFI_TYPE_COL,
	// 3);
	// values.put(
	// NotificationDBController.USERNAME_COL,
	// username);
	// values.put(
	// NotificationDBController.SOHIEUCONGVAN_COL,
	// soHieuCongVan);
	// values.put(
	// NotificationDBController.TRICHYEU_COL,
	// trichYeu);
	// values.put(
	// NotificationDBController.DINHKEM_COL,
	// dinhKem);
	// values.put(
	// NotificationDBController.NGAYDENSICCO_COL,
	// "");
	// values.put(
	// NotificationDBController.TRANGTHAI_COL,
	// NotificationDBController.NOTIFICATION_STATE_NEW);
	//
	// // long rowInserted =
	// db.insert(
	// NotificationDBController.TABLE_NAME,
	// null, values);
	//
	// }
	// }
	// // initMessageData(cacLoai_list, 3, username);
	// boolean firstRun = Utils.getBoolean(
	// getApplicationContext(), "FIRSTRUN", true);
	// if (firstRun) {
	// Utils.saveBoolean(getApplicationContext(),
	// "FIRSTRUN", false);
	// origanizeNoti(cacLoai_list, total, 3);
	// saveInt(3, total);
	// } else {
	// // =================================================
	// // \\
	// Dispatch dis = new Dispatch(
	// getApplicationContext());
	// String url = getApplicationContext()
	// .getResources()
	// .getString(
	// R.string.api_get_dispatch_other);
	// dData = new ArrayList<Dispatch>();
	// dData = dis.getData(getApplicationContext(),
	// url, new OnLoadListener() {
	//
	// @Override
	// public void onSuccess() {
	// cacLoai(username);
	// }
	//
	// @Override
	// public void onStart() {
	// // TODO Auto-generated method
	// // stub
	//
	// }
	//
	// @Override
	// public void onFalse() {
	// // TODO Auto-generated method
	// // stub
	//
	// }
	// }, 1);
	// }
	//
	// if (cursor != null && cursor.getCount() > 0) {
	// } else {
	// // initMessageData(cacLoai_list, 3, username);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// super.onSuccess(statusCode, headers, response);
	// }
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers,
	// Throwable throwable, JSONObject errorResponse) {
	//
	// super.onFailure(statusCode, headers, throwable,
	// errorResponse);
	// }
	//
	// });
	//
	// }

	void CongViecAsync() {
		url_get_congviec = getResources().getString(R.string.api_get_task);
		taskData = new ArrayList<Task>();

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("user_id",
				Utils.getString(getApplicationContext(), "user_id"));
		params.add("username", Utils.getString(getApplicationContext(), "name"));

		client.post(url_get_congviec, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();
				insertedTask = false;
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						total = object.getInt("total");
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							String id = row.getString("id");
							String ten_cong_viec = row
									.getString("ten_cong_viec");
							String nguoi_thuc_hien = row
									.getString("nguoi_thuc_hien");
							String nguoi_xem = row.getString("nguoi_xem");
							String mo_ta = row.getString("mo_ta");
							String trang_thai = row.getString("trang_thai");

							// // add to db
							db = NotificationDBController
									.getInstance(getApplicationContext());
							username = Utils.getString(getApplicationContext(),
									SessionManager.KEY_NAME);

							String sql = "Select * from "
									+ NotificationDBController.TASK_TABLE_NAME
									+ " where "
									+ NotificationDBController.TRANGTHAI_COL
									+ " = \"new\"" + " and "
									+ NotificationDBController.ID_COL + " = "
									+ id + " and "
									+ NotificationDBController.USERNAME_COL
									+ " = \"" + username + "\"";
							cursor = db.rawQuery(sql, null);

							if (cursor != null && cursor.getCount() > 0) {
								
							} else {
								ContentValues values = new ContentValues();
								values.put(NotificationDBController.ID_COL, id);
								values.put(
										NotificationDBController.USERNAME_COL,
										username);
								values.put(
										NotificationDBController.TASK_TENCONGVIEC,
										ten_cong_viec);
								values.put(
										NotificationDBController.TASK_NGUOIXEM,
										nguoi_xem);
								values.put(
										NotificationDBController.TASK_NGUOITHUCHIEN,
										nguoi_thuc_hien);
								values.put(NotificationDBController.TASK_STATE,
										trang_thai);
								values.put(
										NotificationDBController.TRANGTHAI_COL,
										NotificationDBController.NOTIFICATION_STATE_NEW);

								if (trang_thai.equalsIgnoreCase("active")) {
									insertedTask = true;
									db.insert(
											NotificationDBController.TASK_TABLE_NAME,
											null, values);

									taskData.add(new Task(Long.parseLong(id),
											ten_cong_viec, nguoi_thuc_hien,
											nguoi_xem, mo_ta));
								}

							}
						}
						
						
						if(insertedTask == true){
							origanizeCongViecNoti(taskData, taskData.size());
							saveCVInt(taskData.size());
							Log.d("MyDebug", " chay lai service get CV");
						}
//						boolean firstRun = Utils.getBoolean(
//								getApplicationContext(), "FIRSTRUN", true);
//						if (firstRun) {
//							Utils.saveBoolean(getApplicationContext(),
//									"FIRSTRUN", false);
//							origanizeCongViecNoti(taskData, taskData.size());
//							saveCVInt(taskData.size());
//						} else {
//							// =================================================
//							// \\
//							Task task = new Task(getApplicationContext());
//							String url = getResources().getString(
//									R.string.api_get_task);
//							taskData = new ArrayList<Task>();
//							taskData = task.getData(getApplicationContext(),
//									url, new OnLoadListener() {
//
//										@Override
//										public void onSuccess() {
//											CongViec(username);
//										}
//
//										@Override
//										public void onStart() {
//
//										}
//
//										@Override
//										public void onFalse() {
//
//										}
//									});
//						}

//						if (cursor != null && cursor.getCount() > 0) {
//
//						} else {
//							initCVData(taskData, username);
//							// CongViec(username);
//							Log.d("MyDebug", "start CongViec Notification <= ");
//						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				insertedTask = false;
			}
		});

	}

	void CongViec(String username) {
		int count = 0;
		db = NotificationDBController.getInstance(getApplicationContext());
		cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null, null,
				null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.TRANGTHAI_COL + " = \"new\""
				+ " and " + NotificationDBController.TASK_STATE
				+ " = \"active\"" + " and "
				+ NotificationDBController.USERNAME_COL + " = \"" + username
				+ "\"";
		cursor = db.rawQuery(sql, null);
		count = cursor.getCount();
		// ================================================= \\
		// origanizeNoti(cacLoai_list, count);
		origanizeCongViecNoti(taskData, count);
		saveCVInt(count);
	}

	void BinhLuanAsync() {
		db = NotificationDBController.getInstance(getApplicationContext());
		db.deleteReportData();

		url_get_binhluan = getResources().getString(R.string.api_tatcabinhluan);
		reportData = new ArrayList<ReportSteerTask>();
		RequestParams params = new RequestParams();

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url_get_binhluan, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);

							String id = row.getString("id");
							String date = row.getString("thoi_gian");
							String handler = row.getString("nguoi_binh_luan");
							String content = row.getString("noi_dung");
							String id_cv = row.getString("id_cv");

							date = date.substring(0, 10);
							// add to db

							String username = Utils.getString(
									getApplicationContext(),
									SessionManager.KEY_NAME);
							String sql = "Select * from "
									+ NotificationDBController.REPORT_TABLE_NAME
									+ " where "
									+ NotificationDBController.ID_COL + " = "
									+ id + " and "
									+ NotificationDBController.USERNAME_COL
									+ " = \"" + username + "\"";
							cursor = db.rawQuery(sql, null);

							if (cursor != null && cursor.getCount() > 0) {

							} else {
								inserted = true;
								ContentValues values = new ContentValues();
								values.put(NotificationDBController.ID_COL, id);
								values.put(
										NotificationDBController.USERNAME_COL,
										username);
								values.put(
										NotificationDBController.REPORT_CONTENT,
										content);
								values.put(
										NotificationDBController.REPORT_DATE,
										date);
								values.put(
										NotificationDBController.REPORT_HANDLER,
										handler);

								db.insert(
										NotificationDBController.REPORT_TABLE_NAME,
										null, values);

								reportData.add(new ReportSteerTask(Long
										.parseLong(id), handler, content, id_cv));
								// Log.d("LuanDT", "id task add to reportData: "
								// + id_cv);
							}

						}
						if (inserted) {
							int size = reportData.size();
							origanizeBinhLuanNoti(reportData, size);
							// Log.d("MyDebug",
							// "origanizeBinhLuanNoti abcxyzasfasfhkasf" );
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});

	}

	void initCVData(ArrayList<Task> data, String username) {
		db = NotificationDBController.getInstance(getApplicationContext());
		taskData = new ArrayList<Task>();
		cursor = db.query(NotificationDBController.TABLE_NAME, null, null,
				null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.TRANGTHAI_COL + " = \"new\""
				+ " and " + NotificationDBController.TASK_STATE
				+ " = \"active\"" + " and "
				+ NotificationDBController.USERNAME_COL + " = \"" + username
				+ "\"";
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				String id = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.ID_COL));
				String ten_cong_viec = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TASK_TENCONGVIEC));
				String nguoi_xem = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TASK_NGUOIXEM));
				String nguoi_thuc_hien = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TASK_NGUOITHUCHIEN));
				String mo_ta = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TASK_CONTENT));
				// add to arraylist
				taskData.add(new Task(Long.parseLong(id), ten_cong_viec,
						nguoi_thuc_hien, nguoi_xem, mo_ta));
			} while (cursor.moveToNext());
		}

		origanizeCongViecNoti(taskData, taskData.size());
		saveCVInt(taskData.size());
		Log.d("MyDebug", "CongViecAsync : count : " + taskData.size());

	}

	void origanizeBinhLuanNoti(ArrayList<ReportSteerTask> data,
			int notification_count) {
		sereprateBinhLuanList(data, notification_count);
		// notifyBR = new NotifyBR();
		// IntentFilter intentFilter = new IntentFilter("acb");
		// registerReceiver(notifyBR, intentFilter);
		// Intent i = new Intent("acb");
		// sendBroadcast(i);
		Log.d("ToanNM", "sereprateCongViecList");
	}

	void origanizeCongViecNoti(ArrayList<Task> data, int notification_count) {
		sereprateCongViecList(data, notification_count);
		notifyBR = new NotifyBR();
		IntentFilter intentFilter = new IntentFilter("acb");
		registerReceiver(notifyBR, intentFilter);
		Intent i = new Intent("acb");
		sendBroadcast(i);
		Log.d("ToanNM", "sereprateCongViecList");

		getTotalNotification();
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

	void saveCVInt(int size) {
		Utils.saveInt(getApplicationContext(), CV_KEY, size);
	}

	void getTotalNotification() {
		int cvcp_total = Utils.getInt(getApplicationContext(), CVCP_KEY, 0);
		int cvxl_total = Utils.getInt(getApplicationContext(), CVXL_KEY, 0);
		int cv_total = Utils.getInt(getApplicationContext(), CV_KEY, 0);
		int total = cvcp_total + cvxl_total + cv_total;
		Utils.saveInt(getApplicationContext(), TOTAL_KEY, total);
		BadgeUtils.setBadge(getApplicationContext(), total);

	}

	void origanizeNoti(ArrayList<NotificationModel> data,
			int notification_count, int type) {
		sereprateList(data, notification_count, type);
		notifyBR = new NotifyBR();
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

	void sereprateCongViecList(ArrayList<Task> data, int notification_count) {
		int size = data.size();
		if (action == 1 && size != 0) {
			MyNotificationManager myNotificationManager = new MyNotificationManager();
			myNotificationManager.notifyCongViec(getApplicationContext(), data);
			Log.d("ToanNM", "CongViec Notification <=");
		}
	}

	void sereprateBinhLuanList(ArrayList<ReportSteerTask> data,
			int notification_count) {
		int size = data.size();
		if (size != 0) {
			MyNotificationManager myNotificationManager = new MyNotificationManager();
			myNotificationManager.notifyBinhLuan(getApplicationContext(), data);
			Log.d("ToanNM", "BinhLuan Notification <= " + taskData.size());
		}
	}

	void cancelNotification(Context context, int notification_id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(notification_id);
	}

	// getCurrentDate
	String getCurrentDate() {
		String currentDate = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String currentDateandTime = sdf.format(new Date());
			Date cdate = sdf.parse(currentDateandTime);
			Calendar now2 = Calendar.getInstance();
			now2.add(Calendar.DATE, 0);

			int d = now2.get(Calendar.DATE);
			int m = now2.get(Calendar.MONTH) + 1;
			String month = "", date = "";
			if (m < 10) {
				month = "0" + m;
			} else {
				month = "" + m;
			}
			if (d < 10) {
				date = "0" + d;
			} else {
				date = "" + d;
			}

			int year = now2.get(Calendar.YEAR);
			String beforedate = year + "/" + month + "/" + date;
			Date BeforeDate1 = sdf.parse(beforedate);
			cdate.compareTo(BeforeDate1);

			currentDate = beforedate.replace("/", "-");

			Log.d("MyDebug", "secondary List : " + currentDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return currentDate;
	}

}