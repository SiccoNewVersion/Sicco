package com.sicco.erp.model;

public class DuAn {
	String id, tenduan;
	public DuAn(String id, String tenduan) {
		this.id = id;
		this.tenduan = tenduan;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setTenDuAn(String tenduan) {
		this.tenduan = tenduan;
	}
	public String getTenDuAn() {
		return tenduan;
	}

}
