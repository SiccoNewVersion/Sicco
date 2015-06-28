package com.sicco.erp.model;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sicco.erp.model.Dispatch.OnLoadListener;

public class ReportSteer {
	private Context context;
	private String handler,date,content;
	private ArrayList<ReportSteer> data;
	private long id;
	private OnLoadListener onLoadListener;
	
	public ReportSteer(Context context) {
		this.context = context;
	}


	public ReportSteer(long id,String handler, String date, String content) {
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
	
	public ArrayList<ReportSteer> getData(String url,final OnLoadListener onLoadListener){
		this.onLoadListener = onLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<ReportSteer>();
		
		AsyncHttpClient  client = new AsyncHttpClient();
		client.post(url, null,new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				onLoadListener.onSuccess();
				String jsonRead = response.toString();
				
				Log.d("NgaDV", jsonRead);
				if(!jsonRead.isEmpty()){
					try {
						JSONObject jsonObject = new JSONObject(jsonRead);
						JSONArray  jsonArray = jsonObject.getJSONArray("row");
						
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject row = jsonArray.getJSONObject(i);
							
							String handler = row.getString("handler");
							String date = row.getString("date");
							String content = row.getString("content");
						
							data.add(new ReportSteer(i,handler, date, content));
							
							Log.d("NgaDV", data.get(i).getContent());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
				onLoadListener.onFalse();
			}
			
		});
		return data;
	}
}
