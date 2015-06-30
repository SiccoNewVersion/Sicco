package com.sicco.erp;

import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.manager.AlertDialogManager;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.service.ServiceStart;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login;
	private TextView forgotPW;

	private static String url = "http://office.sicco.vn/api/get_one_user.php";
	String login_file;
	// Session Manager Class
	SessionManager session;
	String p = "";
	String u;
	String mToken, mUser_id, mId_phonng_ban;

	ProgressBar mLoginDialog;

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
		// mLoginDialog = new ProgressDialog(this);
		mLoginDialog = (ProgressBar) findViewById(R.id.login_progress);
		mLoginDialog.setVisibility(View.GONE);

		// session
		session = SessionManager.getInstance(getApplicationContext());
		// check session
		HashMap<String, String> hashMap = session.getUserDetails();
		u = hashMap.get(SessionManager.KEY_NAME);
		p = hashMap.get(SessionManager.KEY_PASSWORD);
		Log.d("ToanNM", "u : " + u + ", p : ");
		if (!u.equals("") && !p.equals("")) {
			MyAsync();
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

		u = username.getText().toString().trim().toLowerCase();
		p = password.getText().toString().trim().toLowerCase();

		if (u.trim().length() > 0 && p.trim().length() > 0) {
			MyAsync();
		} else {
			String t = getApplicationContext().getResources().getString(
					R.string.error_title);
			String s = getApplicationContext().getResources().getString(
					R.string.error_null);
			alert.showAlertDialog(LoginActivity.this, t, s, false);
		}
	}

	void MyAsync() {
		AsyncHttpClient handler = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("username", u);
		params.add("password", p);
		mLoginDialog.setVisibility(View.VISIBLE);
		// String login = getApplicationContext().getResources().getString(
		// R.string.progress_msg);
		// mLoginDialog.setTitle(login);
		// mLoginDialog.setMessage(getString(R.string.progress_msg));
		// mLoginDialog.show();
		login.setEnabled(false);

		handler.post(getApplicationContext(), url, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						int error_code = -1;
						String st = response.toString();
						Log.d("ToanNM", "json:" + st);
						// mLoginDialog.dismiss();
						mLoginDialog.setVisibility(View.GONE);
						login.setEnabled(true);
						try {
							JSONObject json = new JSONObject(st);
							error_code = Integer.valueOf(json
									.getString("error"));
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
							String t = getApplicationContext().getResources()
									.getString(R.string.error_title);
							String s = getApplicationContext().getResources()
									.getString(R.string.error);
							alert.showAlertDialog(LoginActivity.this, t, s,
									false);
						}

						super.onSuccess(statusCode, headers, response);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						// mLoginDialog.dismiss();
						mLoginDialog.setVisibility(View.GONE);
						login.setEnabled(true);
						String s = getApplicationContext().getResources()
								.getString(R.string.internet_false);
						String t = getApplicationContext().getResources()
								.getString(R.string.error_title);
						alert.showAlertDialog(LoginActivity.this, t, s, false);

						super.onFailure(statusCode, headers, throwable,
								errorResponse);
					}

				});

	}

}