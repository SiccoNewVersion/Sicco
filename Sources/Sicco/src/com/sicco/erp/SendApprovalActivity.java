package com.sicco.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChoseUser;

public class SendApprovalActivity extends Activity implements OnClickListener {
	private ImageView back, send;
	private EditText document;
	private Department department;
	private User user;
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_send_approval);
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		send = (ImageView) findViewById(R.id.send);
		document = (EditText) findViewById(R.id.document);
		// click
		back.setOnClickListener(this);
		send.setOnClickListener(this);

		department = new Department();
		user = new User(SendApprovalActivity.this);

		listDep = new ArrayList<Department>();
		allUser = new ArrayList<User>();
		listChecked = new ArrayList<User>();

		listDep = department.getData("http://office.sicco.vn/api/departments.php");
		allUser = user.getData("http://office.sicco.vn/api/list_users.php");
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.send:
			new DialogChoseUser(SendApprovalActivity.this, listDep, allUser,
					listChecked);
			break;
		}
	}
}