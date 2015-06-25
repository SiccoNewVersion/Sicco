package com.sicco.erp.model;

public class PhongBan {

	String id, tenphongban;
	public PhongBan(String id, String tenphongban) {
		this.id = id;
		this.tenphongban = tenphongban;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setTenPhongBan(String tenphongban) {
		this.tenphongban = tenphongban;
	}
	public String getTenPhongBan() {
		return tenphongban;
	}
}
