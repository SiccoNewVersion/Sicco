package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.service.ServiceStart;
import com.sicco.erp.util.Utils;

public class HomeActivity extends Activity implements OnClickListener {
	private LinearLayout canphe, xuly, cacloai;
	private FrameLayout exit;
	private AlertDialog alertDialog;
	private Department department;
	private User user;
	public static ArrayList<Department> listDep;
	public static ArrayList<User> allUser;

	// ToanNM
	private int cvcp_count, cvxl_count, cl_count;
	private SessionManager session;
	private static HomeActivity homeActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		// ToanNM
		ServiceStart.startGetNotificationService(getApplicationContext());
		Log.d("ToanNM", "Service has been started from Home Activity");
		setContentView(R.layout.activity_home);
		homeActivity = this;
		session = SessionManager.getInstance(getApplicationContext());
		init();
		department = new Department();
		user = new User();
		listDep = new ArrayList<Department>();
		allUser = new ArrayList<User>();
		listDep = department.getData(getResources().getString(
				R.string.api_get_deparment));
		allUser = user.getData(getResources().getString(
				R.string.api_get_all_user));
	}

	public static HomeActivity getInstance() {
		return homeActivity;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	void checkNotifyCount(TextView textView, int notifyCount) {
		if (notifyCount != 0) {
			textView.setVisibility(View.VISIBLE);
			textView.setText("" + notifyCount);
		} else if (notifyCount == 0) {
			textView.setVisibility(View.GONE);
		}
	}

	private void init() {
		// view
		canphe = (LinearLayout) findViewById(R.id.canphe);
		xuly = (LinearLayout) findViewById(R.id.xuly);
		cacloai = (LinearLayout) findViewById(R.id.cacloai);
		// option = (FrameLayout) findViewById(R.id.option);
		exit = (FrameLayout) findViewById(R.id.exit);
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
		case R.id.exit:
			session.logoutUser();
			ServiceStart.stopAllService(getApplicationContext());
			Utils.stopAlarm(getApplicationContext());
			finish();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
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

	private void setCountNotify() {
		cvcp_count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CVCP_KEY);
		cvxl_count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CVXL_KEY);
		cl_count = Utils.getInt(getApplicationContext(),
				GetAllNotificationService.CL_KEY);
		TextView notify_cvcp = (TextView) findViewById(R.id.activity_home_notify_canphe);
		TextView notify_cvxl = (TextView) findViewById(R.id.activity_home_notify_xuly);
		TextView notify_cl = (TextView) findViewById(R.id.activity_home_notify_cacloai);
		checkNotifyCount(notify_cvcp, cvcp_count);
		checkNotifyCount(notify_cvxl, cvxl_count);
		checkNotifyCount(notify_cl, cl_count);
	}

	public static class NotifyBR extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (HomeActivity.getInstance() != null)
				HomeActivity.getInstance().setCountNotify();
		}
	}
}