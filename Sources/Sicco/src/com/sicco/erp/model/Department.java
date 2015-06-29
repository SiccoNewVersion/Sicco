package com.sicco.erp.model;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class Department {

	private String id, departmentName;
	private ArrayList<Department> listDep;
	public static boolean getJsonDep = false;

	public Department() {
	}
	
	public Department(String id, String departmentName) {
		this.id = id;
		this.departmentName = departmentName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public ArrayList<Department> getData( final String url) {
		listDep = new ArrayList<Department>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, null, new JsonHttpResponseHandler() {
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
							String id = row.getString("id_phong_ban");
							String depName = row.getString("ten_phong_ban");
							
							listDep.add(new Department(id, depName));
							
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				getJsonDep = true;
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
//				getData(url);
				getJsonDep = false;
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.d("LuanDT", "json: false");
			}
		});
		return listDep;
	}
	
	public ArrayList<Department> getData(final String url, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		listDep = new ArrayList<Department>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, null, new JsonHttpResponseHandler() {
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
							String id = row.getString("id_phong_ban");
							String depName = row.getString("ten_phong_ban");
							
							listDep.add(new Department(id, depName));
							
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
				onLoadListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.d("LuanDT", "json: false");
			}
		});
		return listDep;
	}
	
	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
