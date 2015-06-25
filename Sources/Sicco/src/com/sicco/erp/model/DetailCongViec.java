package com.sicco.erp.model;

public class DetailCongViec {
	int total;
	String ID;
	String title;
	String code;
	String mota;
	String status;
	String dateStart;
	String dateEnd;
	
	public DetailCongViec(String ID, String title, String code, String mota, String status, String dateStart, String dateEnd) {
		this.ID = ID;
		this.title = title;
		this.code = code;
		this.mota = mota;
		this.status = status;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal() {
		return total;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getID() {
		return ID;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setMota(String mota) {
		this.mota = mota;
	}
	public String getMota() {
		return mota;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus() {
		return status;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getDateEnd() {
		return dateEnd;
	}
}
