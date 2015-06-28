package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sicco.erp.adapter.TaskAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChoseUser;

public class SendApprovalActivity extends Activity implements OnClickListener {
	private ImageView back, send;
	private EditText document;
	private Button btnChoseHandler, btnApproval;
	public static TextView txtHandler;
	private Department department;
	private User user;
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked;
	private String idHandler = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_send_approval);
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		// send = (ImageView) findViewById(R.id.send);
		btnChoseHandler = (Button) findViewById(R.id.btnChoseHandler);
		btnApproval = (Button) findViewById(R.id.btnApproval);
		txtHandler = (TextView) findViewById(R.id.txt_handler);
		document = (EditText) findViewById(R.id.document);
		// click
		back.setOnClickListener(this);
//		send.setOnClickListener(this);
		btnApproval.setOnClickListener(this);
		btnChoseHandler.setOnClickListener(this);

		department = new Department();
		user = new User(SendApprovalActivity.this);

		listChecked = new ArrayList<User>();

		listDep = HomeActivity.listDep;
		allUser = HomeActivity.allUser;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.btnChoseHandler:
			TaskAdapter.flag = "";
			new DialogChoseUser(SendApprovalActivity.this, listDep, allUser,
					listChecked);
			break;
		case R.id.btnApproval:
			for (int i = 0; i < listChecked.size(); i++) {
				idHandler += listChecked.get(i).getId() + ";";
			}
			break;
			
		default:
			break;
		}
	}

}