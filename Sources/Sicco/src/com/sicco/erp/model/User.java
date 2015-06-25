package com.sicco.erp.model;

import android.content.Context;

public class User {
	public static int LOGIN_FALSE = 0;
	public static int LOGIN_SUCCESS = 1;

	private Context context;
	private String username;
	private String password;

	public User(Context context) {
		this.context = context;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int login() {
		if (username.equals("admin")) {
			return LOGIN_SUCCESS;
		} else {
			return LOGIN_FALSE;
		}

	}
}
