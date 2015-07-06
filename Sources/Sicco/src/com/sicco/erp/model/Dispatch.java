package com.sicco.erp.model;

import java.io.Serializable;
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
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.util.Utils;

public class Dispatch implements Serializable {
	private Context context;
	private long id;
	private String numberDispatch, description, content;
	private String date, handler;
	private String status;
	private ArrayList<Dispatch> data;
	private Utils utils;

	public Dispatch(Context context) {
		this.context = context;
	}

	public Dispatch(long id, String numberDispatch, String description,
			String content, String date, String handler, String status) {
		super();
		this.id = id;
		this.numberDispatch = numberDispatch;
		this.description = description;
		this.content = content;
		this.date = date;
		this.handler = handler;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public String getNumberDispatch() {
		return numberDispatch;
	}

	public void setNumberDispatch(String numberDispatch) {
		this.numberDispatch = numberDispatch;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Dispatch> getData() {
		return data;
	}

	public void setData(ArrayList<Dispatch> data) {
		this.data = data;
	}

	public ArrayList<Dispatch> getData(String url, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<Dispatch>();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("username", utils.getString(context, "name"));
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
							long id = row.getInt("id");
							String numberDispatch = row.getString("so_hieu");
							String description = row.getString("trich_yeu");
							String content = row.getString("content");
							String date = row.getString("ngay_den");
							String status = row.getString("status");
							String handler = row.getString("handler");
							
							data.add(new Dispatch(id, numberDispatch, description, content, date, handler, status));
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
				Log.d("TuNT", "json: false");
			}
		});
		return data;
	}

	public ArrayList<Dispatch> search(String k, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		this.onLoadListener.onStart();
		ArrayList<Dispatch> result = new ArrayList<Dispatch>();
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Dispatch dispatch : data) {
					if (dispatch.getNumberDispatch().contains(k)) {
						result.add(dispatch);
					}
				}
			}
		}
		this.onLoadListener.onSuccess();
		return result;
	}

	public ArrayList<Dispatch> search(String k) {
		ArrayList<Dispatch> result = new ArrayList<Dispatch>();
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Dispatch dispatch : data) {
					if (dispatch.getNumberDispatch().contains(k)) {
						result.add(dispatch);
					}
				}
			}
		}
		return result;
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
