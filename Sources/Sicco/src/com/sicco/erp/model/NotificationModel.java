package com.sicco.erp.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	String notify_type;
	String msg_type;
	String content;
	String url;
	String state;
	String name;

	public NotificationModel(long id, String notify, String msg, String name, String content, String url, String state) {
		this.id = id;
		this.notify_type = notify;
		this.msg_type = msg;
		this.name = name;
		this.content = content;
		this.url = url;
		this.state = state;
		
	}
	
	public long getId() {
		return id;
	}




	public void setId(long id) {
		this.id = id;
	}




	public String getNotify_type() {
		return notify_type;
	}




	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}




	public String getMsg_type() {
		return msg_type;
	}




	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}




	public String getContent() {
		return content;
	}




	public void setContent(String content) {
		this.content = content;
	}




	public String getUrl() {
		return url;
	}




	public void setUrl(String url) {
		this.url = url;
	}




	public String getState() {
		return state;
	}




	public void setState(String state) {
		this.state = state;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public boolean equalsContent(NotificationModel other) {
		return ((this.id == other.id))
				&& (this.notify_type.equals(other.notify_type))
				&& (this.msg_type.equals(other.msg_type)) 
				&& (this.name.equals(other.name)) 
				&& (this.content.equals(other.content))
				&& (this.url.equals(other.url));
	}

	public String toString() {
		String ret = "" + content;
		return ret;
	}
	
}
