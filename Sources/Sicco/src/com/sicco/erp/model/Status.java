package com.sicco.erp.model;

public class Status {
	private String status,key;

	
	
	public Status(String status, String key) {
		super();
		this.status = status;
		this.key = key;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
