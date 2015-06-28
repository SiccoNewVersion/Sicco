package com.sicco.erp;

import java.util.ArrayList;

import com.sicco.erp.R;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener {
	private LinearLayout canphe, xuly, cacloai;
	private FrameLayout option, exit;
	private AlertDialog alertDialog;
	private Department department;
	private User user;
	public static ArrayList<Department> listDep;
	public static ArrayList<User> allUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_home);
		init();
		
		//init data
		department = new Department();
		user = new User();
		listDep = new ArrayList<Department>();
		allUser = new ArrayList<User>();
		listDep = department
				.getData("http://office.sicco.vn/api/departments.php");
		allUser = user.getData("http://office.sicco.vn/api/list_users.php");
	}

	private void init() {
		//view
		canphe = (LinearLayout) findViewById(R.id.canphe);
		xuly = (LinearLayout) findViewById(R.id.xuly);
		cacloai = (LinearLayout) findViewById(R.id.cacloai);
//		option = (FrameLayout) findViewById(R.id.option);
		exit = (FrameLayout) findViewById(R.id.exit);
		//click
		canphe.setOnClickListener(this);
		xuly.setOnClickListener(this);
		cacloai.setOnClickListener(this);
//		option.setOnClickListener(this);
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
//		case R.id.option:
//			startActivity(OptionActivity.class);
//			break;
		case R.id.exit:
			showDialogConfirmExit();
			break;
		}
	}
	@Override
	public void onBackPressed() {
		showDialogConfirmExit();
	}
	private void startActivity(Class c){
		Intent intent = new Intent(HomeActivity.this, c);
		startActivity(intent);
	}
	private void exit(){
		System.exit(0);
	}
	private void showDialogConfirmExit(){
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
