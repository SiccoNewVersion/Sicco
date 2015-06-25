package com.sicco.erp;

import com.sicco.erp.model.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login;
	private TextView forgotPW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		forgotPW = (TextView) findViewById(R.id.forgot_pw);
		// click
		login.setOnClickListener(this);
		forgotPW.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login:
			login();
			break;
		case R.id.forgot_pw:
			Toast.makeText(LoginActivity.this, "forgot_pw",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void login() {
		String u = username.getText().toString().trim().toLowerCase();
		String p = password.getText().toString().trim().toLowerCase();
		User user = new User(LoginActivity.this);
		user.setUsername(u);
		user.setPassword(p);
		int login = user.login();
		if (login == User.LOGIN_SUCCESS) {
			finish();
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(LoginActivity.this, "sai tai khoan hoac mat khau",
					Toast.LENGTH_SHORT).show();
		}
	}
}
