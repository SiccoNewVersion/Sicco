package com.sicco.erp.model;

import java.util.ArrayList;

import android.content.Context;

public class Dispatch {
	private Context context;
	private long id;
	private String title, description, content;
	private ArrayList<String> image_url;
	private String time;
	private int status;
	
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
	public ArrayList<Dispatch> getData(){
		ArrayList<Dispatch> data = new ArrayList<Dispatch>();

		ArrayList<String> url = new ArrayList<String>();
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		data.add(new Dispatch(1, "title", "description", "content", url, "time", 1));
		return data;
	}
}
