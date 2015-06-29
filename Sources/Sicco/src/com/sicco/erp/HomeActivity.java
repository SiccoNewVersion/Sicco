package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;
import com.sicco.erp.service.GetAllNotificationService;

public class HomeActivity extends Activity implements OnClickListener {
	private LinearLayout canphe, xuly, cacloai, exit;
	private FrameLayout option;
	private AlertDialog alertDialog;
	private Department department;
	private User user;
	public static ArrayList<Department> listDep;
	public static ArrayList<User> allUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		// ToanNM
		Intent intent = new Intent(this, GetAllNotificationService.class);
		startService(intent);
		Log.d("ToanNM", "Service has been started from Home Activity");
		setContentView(R.layout.activity_home);
		setCount();
		// === End of ToanNM ===
		init();
		init();

		// init data
		department = new Department();
		user = new User();
		listDep = new ArrayList<Department>();
		allUser = new ArrayList<User>();
		listDep = department
				.getData("http://office.sicco.vn/api/departments.php");
		allUser = user.getData("http://office.sicco.vn/api/list_users.php");
	}

	void setCount() {
		// ToanNM
		int delay = 1000;
		final TextView notify_cvcp = (TextView) findViewById(R.id.activity_home_notify_canphe);
		final TextView notify_cvxl = (TextView) findViewById(R.id.activity_home_notify_xuly);
		final TextView notify_cl = (TextView) findViewById(R.id.activity_home_notify_cacloai);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				notify_cvcp.setText(""
						+ GetAllNotificationService.getCongViecCanPheCount());
				notify_cvxl.setText(""
						+ GetAllNotificationService.getCongViecCanXuLyCount());
				notify_cl.setText(""
						+ GetAllNotificationService.getCacLoaiCount());
			}
		}, delay);

		// === End of ToanNM ===
	}

	private void init() {
		// view
		canphe = (LinearLayout) findViewById(R.id.canphe);
		xuly = (LinearLayout) findViewById(R.id.xuly);
		cacloai = (LinearLayout) findViewById(R.id.cacloai);
		// option = (FrameLayout) findViewById(R.id.option);
		exit = (LinearLayout) findViewById(R.id.exit);
		// click
		canphe.setOnClickListener(this);
		xuly.setOnClickListener(this);
		cacloai.setOnClickListener(this);
		// option.setOnClickListener(this);
		exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.canphe:
			startActivity(ApprovalActivity.class);
			break;
		case R.id.xuly:
			startActivity(DealtWithActivity.class);
			break;
		case R.id.cacloai:
			startActivity(OtherActivity.class);
			break;
		// case R.id.option:
		// startActivity(OptionActivity.class);
		// break;
		case R.id.exit:
			// showDialogConfirmExit();
			Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// showDialogConfirmExit();
	}

	private void startActivity(Class c) {
		Intent intent = new Intent(HomeActivity.this, c);
		startActivity(intent);
	}

	private void exit() {
		System.exit(0);
	}

	private void showDialogConfirmExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(HomeActivity.this,
						android.R.style.Theme_Holo_Light));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle(R.string.app_name);
		builder.setMessage(R.string.confirm_exit);
		builder.setCancelable(true);
		builder.setPositiveButton(R.string.exit,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						exit();
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						alertDialog.dismiss();
					}
				});
		alertDialog = builder.create();
		alertDialog.show();
	}
}
