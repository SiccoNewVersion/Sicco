package com.sicco.erp.model;

public class Status {
	private String status;
	private Long key;

	
	
	public Status(String status, Long key) {
		super();
		this.status = status;
		this.key = key;
	}

	public Status() {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getKey() {
		return key;
	}

	public void setKey(Long key) {
		this.key = key;
	}

}
