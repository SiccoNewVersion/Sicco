package com.sicco.erp.model;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class Dispatch {
	private Context context;
	private long id;
	private String title, description, content;
	private ArrayList<String> image_url;
	private String time;
	private int status;
	private ArrayList<Dispatch> data;

	public Dispatch(Context context) {
		this.context = context;
	}

	public Dispatch(long id, String title, String description, String content,
			ArrayList<String> image_url, String time, int status) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.content = content;
		this.image_url = image_url;
		this.time = time;
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
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

	public ArrayList<String> getImage_url() {
		return image_url;
	}

	public void setImage_url(ArrayList<String> image_url) {
		this.image_url = image_url;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setData(ArrayList<Dispatch> data) {
		this.data = data;
	}

	public ArrayList<Dispatch> getData(String url, OnLoadListener OnLoadListener) {
		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<Dispatch>();
		final ArrayList<String> imageUrl = new ArrayList<String>();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, null,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						onLoadListener.onFinish();
						String jsonRead = response.toString();

						Log.d("TuNT", "json: " + jsonRead);
						if (!jsonRead.isEmpty()) {
							try {
								JSONObject object = new JSONObject(jsonRead);
								JSONArray array = object.getJSONArray("row");
								for (int i = 0; i < array.length(); i++) {
									JSONObject rows = array.getJSONObject(i);
									String title = rows.getString("title");
									String description = rows
											.getString("description");
									data.add(new Dispatch(1, title,
											description, "content", imageUrl,
											"time", 1));
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
						super.onFailure(statusCode, headers, throwable,
								errorResponse);
						onLoadListener.onFinish();
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
					if (dispatch.getTitle().contains(k)) {
						result.add(dispatch);
					}
				}
			}
		}
		this.onLoadListener.onFinish();
		return result;
	}

	public ArrayList<Dispatch> search(String k) {
		ArrayList<Dispatch> result = new ArrayList<Dispatch>();
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Dispatch dispatch : data) {
					if (dispatch.getTitle().contains(k)) {
						result.add(dispatch);
					}
				}
			}
		}
		return result;
	}

	public interface OnLoadListener {
		void onStart();

		void onFinish();
	}

	private OnLoadListener onLoadListener;
}
