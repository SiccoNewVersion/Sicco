package com.sicco.erp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ReportSteer {
	private Context context;
	private String handler, date, content;
	private ArrayList<ReportSteer> data;
	private long id;

	public ReportSteer(Context context) {
		this.context = context;
	}

	public ReportSteer(long id, String handler, String date, String content) {
		this.id = id;
		this.handler = handler;
		this.date = date;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public ArrayList<ReportSteer> getData() {
		return data;
	}

	public void setData(ArrayList<ReportSteer> data) {
		this.data = data;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	//sendReport
	public void sendReportSteer(String url,String id_user,String id_dispatch,String content,OnLoadListener OnLoadListener){
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		RequestParams params = new RequestParams();
		params.add("id_user", id_user);
		params.add("id_dispatch", id_dispatch);
		params.add("content", content);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				onLoadListener.onSuccess();
				String jsonRead = response.toString();
				
				Log.d("NgaDV", "get Report Json : " + jsonRead);
//				if (!jsonRead.isEmpty()) {
//					try {
//						JSONObject jsonObject = new JSONObject(jsonRead);
//						JSONArray jsonArray = jsonObject.getJSONArray("row");
//
//						for (int i = 0; i < jsonArray.length(); i++) {
//							JSONObject row = jsonArray.getJSONObject(i);
//
//							String reporter = row.getString("reporter");
//							String date = row.getString("date_created");
//							String content = row.getString("content");
//
//							data.add(new ReportSteer(i, reporter, date, content));
//
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				onLoadListener.onFalse();
			}

		});
	}

	//getData
	public ArrayList<ReportSteer> getData(String url,String id_user,String id_dispatch,
			OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<ReportSteer>();
		
		RequestParams params = new RequestParams();
		params.add("id_user", id_user);
		params.add("id_dispatch", id_dispatch);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				onLoadListener.onSuccess();
				String jsonRead = response.toString();
				
				Log.d("NgaDV", "get Report Json : " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject jsonObject = new JSONObject(jsonRead);
						JSONArray jsonArray = jsonObject.getJSONArray("row");

						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject row = jsonArray.getJSONObject(i);

							String reporter = row.getString("reporter");
							String dateCreated = row.getString("date_created");
							String content = row.getString("content");

							data.add(new ReportSteer(i, reporter, dateCreated, content));

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				onLoadListener.onFalse();
			}

		});
		return data;
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
