package com.sicco.erp.model;

public class CongViec {
	int image;
	String title;
	public CongViec(int id, String title) {
		this.image = id;
		this.title = title;
	}
	public void setImage(int id) {
		this.image = id;
	}
	public int getImage() {
		return image;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
}
