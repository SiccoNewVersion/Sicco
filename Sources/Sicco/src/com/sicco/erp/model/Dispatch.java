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
import com.sicco.erp.R;
import com.sicco.erp.util.AccentRemover;
import com.sicco.erp.util.Utils;

public class Dispatch implements Serializable {
	private Context context;
	private long id;
	private String numberDispatch, description, content;
	private String date, handler;
	private String status;
	private ArrayList<Dispatch> data;

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
		params.add("username", Utils.getString(context, "name"));
		Log.d("LuanDT", "params: " + params);
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

							data.add(new Dispatch(id, numberDispatch,
									description, content, date, handler, status));
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
		String vReplace = AccentRemover.getInstance(context).removeAccent(k);
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Dispatch dispatch : data) {

					String iReplace = AccentRemover.getInstance(context)
							.removeAccent(dispatch.getNumberDispatch());
					String replace = AccentRemover.getInstance(context)
							.removeAccent(dispatch.getDescription());
					if (iReplace.toLowerCase().contains(vReplace.toLowerCase())
							|| replace.toLowerCase().contains(
									vReplace.toLowerCase())) {
						result.add(dispatch);
					}
				}
			}
		}
		return result;
	}

	public void guiXuLy(String url, String cvId, String nguoiXuLy,
			OnRequestListener onRequestListener) {
		Dispatch.this.onRequestListener = onRequestListener;
		Dispatch.this.onRequestListener.onStart();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("id_cv", cvId);
		params.add("nguoi_xu_ly", nguoiXuLy);

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject json = new JSONObject(jsonRead);
						int error = json.getInt("error");
						if (error == 1) {
							Dispatch.this.onRequestListener.onFalse(json.getString("error_msg"));
						} else {
							Dispatch.this.onRequestListener.onSuccess();
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
				Dispatch.this.onRequestListener.onFalse();
			}
		});
	}
	
	public void approvalDispatch(String url, String userId, String cvId, String noiDung, String nguoiXuLy, OnRequestListener onRequestListener){
		Dispatch.this.onRequestListener = onRequestListener;
		Dispatch.this.onRequestListener.onStart();
		RequestParams params = new RequestParams();
		params.add("id", userId);
		params.add("id_cv", cvId);
		params.add("noi_dung", noiDung);
		params.add("nguoi_xu_ly", nguoiXuLy);
		
		Log.d("LuanDT", "params phe: " + params);
		
		AsyncHttpClient httpClient = new AsyncHttpClient();
		httpClient.post(url, params, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				Dispatch.this.onRequestListener.onFalse();
				super.onFailure(statusCode, headers, throwable, errorResponse);
				Log.d("LuanDT", "json: false");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String json = response.toString();
				Log.d("LuanDT", "json phe cv: " + json);
				
				if(!json.isEmpty()){
					try {
						JSONObject object = new JSONObject(json);
						int success = object.getInt("success");
						if(success == 1){
							Dispatch.this.onRequestListener.onSuccess();
						} else {
							Dispatch.this.onRequestListener.onFalse(context.getResources().getString(R.string.error_l));
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				super.onSuccess(statusCode, headers, response);
			}
			
		});
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	public interface OnRequestListener {
		void onStart();

		void onFalse(String stFalse);

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
	private OnRequestListener onRequestListener;
}
