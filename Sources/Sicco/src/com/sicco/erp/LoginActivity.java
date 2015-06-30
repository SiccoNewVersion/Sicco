package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.http.HTTPHandler;
import com.sicco.erp.manager.AlertDialogManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.service.ServiceStart;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login;
	private TextView forgotPW;

	// Session Manager Class
	SessionManager session;
	String p = "";
	String u;
	String mToken, mUser_id, mId_phonng_ban;

	ProgressDialog mLoginDialog;

	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

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

		// progress
		mLoginDialog = new ProgressDialog(this);
		mLoginDialog.setTitle("");
		mLoginDialog.setMessage(getString(R.string.progress_msg));

		// session
		session = SessionManager.getInstance(getApplicationContext());
		// check session
		HashMap<String, String> hashMap = session.getUserDetails();
		u = hashMap.get(SessionManager.KEY_NAME);
		p = hashMap.get(SessionManager.KEY_PASSWORD);
		Log.d("ToanNM", "u : " + u + ", p : ");
		if (!u.equals("") && !p.equals("")) {
			new MyAsync().execute(u, p);
		}

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.login:
			login();
			break;
		case R.id.forgot_pw:
			Toast.makeText(LoginActivity.this, "forgot_pw", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	private void login() {
		// String u = username.getText().toString().trim().toLowerCase();
		// String p = password.getText().toString().trim().toLowerCase();
		// User user = new User(LoginActivity.this);
		// user.setUsername(u);
		// user.setPassword(p);
		// int login = user.login();
		// if (login == User.LOGIN_SUCCESS) {
		// finish();
		// Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
		// startActivity(intent);
		// } else {
		// Toast.makeText(LoginActivity.this, "sai tai khoan hoac mat khau",
		// Toast.LENGTH_SHORT).show();
		// }

		u = username.getText().toString().trim().toLowerCase();
		p = password.getText().toString().trim().toLowerCase();

		if (u.trim().length() > 0 && p.trim().length() > 0) {
			new MyAsync().execute(u, p);
		} else {
			String t = getApplicationContext().getResources().getString(
					R.string.error_title);
			String s = getApplicationContext().getResources().getString(
					R.string.error_null);
			alert.showAlertDialog(LoginActivity.this, t, s, false);
		}
	}

	public class MyAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			mLoginDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			mLoginDialog.dismiss();
			HTTPHandler handler = new HTTPHandler();
			List<NameValuePair> nameValue = new ArrayList<NameValuePair>();
			nameValue.add(new BasicNameValuePair("username", u));
			nameValue.add(new BasicNameValuePair("password", p));

			String ret = handler.makeHTTPRequest(
					"http://office.sicco.vn/api/get_one_user.php",
					HTTPHandler.POST, nameValue);
			// Log.d("DungHV", "ret = " + ret);

			return ret;
		}

		@Override
		protected void onPostExecute(String result) {
			int error_code = -1;
			int a = 10;
			try {
				JSONObject json = new JSONObject(result);
				error_code = Integer.valueOf(json.getString("error"));
				u = json.getString("username");
				mToken = json.getString("token");
				mUser_id = json.getString("id");
				mId_phonng_ban = json.getString("phongban");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (error_code == 0) {
				session.createLoginSession(u, p, mToken, mUser_id,
						mId_phonng_ban, true);

				Intent i = new Intent(getApplicationContext(),
						HomeActivity.class);
				startActivity(i);
				ServiceStart
						.startGetNotificationService(getApplicationContext());
				finish();
			} else if (error_code != 0) {
				Log.d("ToanNM", "run");
				String t = getApplicationContext().getResources().getString(
						R.string.error_title);
				String s = getApplicationContext().getResources().getString(
						R.string.error);
				alert.showAlertDialog(LoginActivity.this, t, s, false);
			}

			super.onPostExecute(result);
		}
	}
}