package com.sicco.erp.model;

public class CongVan {
	String mTitle,mDetail,mUrl,mId;
	
	public CongVan(String title,String detail,String url,String id) {
		
		this.mTitle = title;
		this.mDetail = detail;
		this.mUrl = url;
		this.mId = id;
	}
	//--------------------Title-----------------------------//
	public void setTitle(String title) {
		this.mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}
	//--------------------Detail-----------------------------//
	public void setDetail(String detail) {
		this.mDetail = detail;
	}
	
	public String getDetail() {
		return mDetail;
	}
	//--------------------Title-----------------------------//
	public void setUrl(String url) {
		this.mUrl = url;
	}
	
	public String getUrl() {
		return mUrl;
	}
	//--------------------Title-----------------------------//
	public void setId(String id) {
		this.mId = id;
	}
	
	public String getId() {
			return mId;
		}
}
