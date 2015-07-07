package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.TaskAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.Dispatch.OnRequestListener;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChoseUser;
import com.sicco.erp.util.Utils;

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
	private String nameHandler = "", returned;
	private Dispatch dispat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_send_approval);
		init();
	}

	private void init() {
		Intent intent = getIntent();
		dispat = (Dispatch) intent.getSerializableExtra("dispatch");

		back = (ImageView) findViewById(R.id.back);
		// send = (ImageView) findViewById(R.id.send);
		btnChoseHandler = (Button) findViewById(R.id.btnChoseHandler);
		btnApproval = (Button) findViewById(R.id.btnApproval);
		txtHandler = (TextView) findViewById(R.id.txt_handler);
		document = (EditText) findViewById(R.id.document);
		// click
		back.setOnClickListener(this);
		// send.setOnClickListener(this);
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
				if (i == listChecked.size() - 1) {
					nameHandler += listChecked.get(i).getUsername();
				} else {
					nameHandler += listChecked.get(i).getUsername() + ",";
				}
			}

			final ProgressDialog progressDialog = new ProgressDialog(
					SendApprovalActivity.this);
			progressDialog.setMessage(getResources()
					.getString(R.string.waiting));

			Dispatch dispatch = new Dispatch(SendApprovalActivity.this);
			dispatch.approvalDispatch(
					getResources().getString(R.string.api_phecongvan),
					Utils.getString(SendApprovalActivity.this, "user_id"), ""
							+ dispat.getId(), document.getText().toString(),
					nameHandler, new OnRequestListener() {

						@Override
						public void onSuccess() {
							progressDialog.dismiss();
							Toast.makeText(SendApprovalActivity.this,
									getResources().getString(R.string.success),
									Toast.LENGTH_LONG).show();
							listChecked.removeAll(listChecked);
							finish();

						}

						@Override
						public void onStart() {
							progressDialog.show();

						}

						@Override
						public void onFalse() {
							listChecked.removeAll(listChecked);
							progressDialog.dismiss();
							Toast.makeText(
									SendApprovalActivity.this,
									getResources().getString(
											R.string.internet_false),
									Toast.LENGTH_LONG).show();
							listChecked.removeAll(listChecked);

						}

						@Override
						public void onFalse(String stFalse) {
							progressDialog.dismiss();
							Toast.makeText(SendApprovalActivity.this, stFalse,
									Toast.LENGTH_LONG).show();
							listChecked.removeAll(listChecked);

						}
					});

			break;

		default:
			break;
		}
	}

}