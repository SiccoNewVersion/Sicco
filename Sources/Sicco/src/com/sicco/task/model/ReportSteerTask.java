package com.sicco.task.model;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.util.Utils;

public class ReportSteerTask {
	private Context context;
	private long id;
	private String handler;
	private String date;
	private String content;
	private String file;
	private ArrayList<ReportSteerTask> data;

	public ReportSteerTask(Context context) {
		this.context = context;
	}

	public ReportSteerTask(long id, String handler, String date,
			String content, String file) {
		super();
		this.id = id;
		this.handler = handler;
		this.date = date;
		this.content = content;
		this.file = file;
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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public ArrayList<ReportSteerTask> getData() {
		return data;
	}

	public void setData(ArrayList<ReportSteerTask> data) {
		this.data = data;
	}

	
	//getData
	public ArrayList<ReportSteerTask> getData(final Context context, String url, String id_task,
			OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<ReportSteerTask>();
		
		RequestParams params = new RequestParams();
		params.add("user_id", Utils.getString(context, "user_id"));
		params.add("id_task", id_task);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				Log.d("LuanDT", "json: " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							
							String id = row.getString("id");
							String handler = row.getString("nguoi_chi_dao");
							String date = row.getString("date");
							String content = row.getString("content");
							String file = row.getString("file");
							
							file = file.replace(" ", "%20");

							data.add(new ReportSteerTask(Long.parseLong(id), handler, date, content, file));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				onLoadListener.onSuccess();
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