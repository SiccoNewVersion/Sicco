package com.sicco.task.erp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnRequestListener;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChoseDepartment;
import com.sicco.erp.util.DialogChoseHandler;
import com.sicco.erp.util.DialogChoseUser;
import com.sicco.erp.util.Utils;

public class AssignTaskActivity extends Activity implements
		OnClickListener {
	private ImageView back;
	private LinearLayout 	lnDateHandle,
							lnHandler, 
							lnViewer, 
							lnDepartment, 
							lnDateFinish,
							lnFile,
							lnProject;
	
	private TextView 	txtDateHandle;
	private TextView 	txtDateFinish;
	public static TextView txtDepartment;
	private EditText 	edtDes;
	private EditText 	edtTitle;
	private Button 		btnAssign;
	private TextView 	txtFile;
	private TextView 	txtProject;

	public static TextView txtHandler, txtViewer;

	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked, listCheckedHandler;
	private Dispatch dispatch;

	static final int DATE_DIALOG_ID = 111;
	private int date;
	private int months;
	private int years_now;

	private StringBuilder toDate;
	private Department department;
	private User user;
	private String file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assign_task);
//
//		Intent intent = getIntent();
//		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
//		file = dispatch.getContent();
		init();
	}

	private void init() {
		back 			= (ImageView) 		findViewById(R.id.back);
		
		lnDateHandle 	= (LinearLayout) 	findViewById(R.id.lnDateHandle);
		lnDateFinish 	= (LinearLayout) 	findViewById(R.id.lnDateFinish);
		lnHandler 		= (LinearLayout) 	findViewById(R.id.lnHandler);
		lnViewer 		= (LinearLayout) 	findViewById(R.id.lnViewer);
		lnDepartment 	= (LinearLayout) 	findViewById(R.id.lnDepartment);
		lnFile 			= (LinearLayout) 	findViewById(R.id.lnFile);
		lnProject 		= (LinearLayout) 	findViewById(R.id.lnProject);

		txtDateHandle 	= (TextView) 		findViewById(R.id.txtDateHandle);
		txtDateFinish 	= (TextView) 		findViewById(R.id.txtDateFinish);
		txtHandler 		= (TextView) 		findViewById(R.id.txtHandler);
		txtViewer 		= (TextView) 		findViewById(R.id.txtViewer);
		txtDepartment 	= (TextView) 		findViewById(R.id.txtDepartment);
		txtFile 		= (TextView)		findViewById(R.id.txtFile);
		txtProject 		= (TextView)		findViewById(R.id.txtProject);
		
		edtDes			= (EditText)		findViewById(R.id.edtDes);
		edtTitle		= (EditText)		findViewById(R.id.edtTitle);
		
		btnAssign		= (Button)			findViewById(R.id.btnAssign);
		

		// click
		back.setOnClickListener(this);

		lnDateHandle.setOnClickListener(this);
		lnDateFinish.setOnClickListener(this);
		lnHandler.setOnClickListener(this);
		lnViewer.setOnClickListener(this);
		lnDepartment.setOnClickListener(this);
		lnFile.setOnClickListener(this);
		lnProject.setOnClickListener(this);
		
		btnAssign.setOnClickListener(this);

//		listChecked = new ArrayList<User>();
//		listCheckedHandler = new ArrayList<User>();
//
//		department = new Department();
//		user = new User();
//		listDep = new ArrayList<Department>();
//		allUser = new ArrayList<User>();
//		listDep = department.getData(getResources().getString(
//				R.string.api_get_deparment));
//		allUser = user.getData(getResources().getString(
//				R.string.api_get_all_user));
//
//		// set data
//		final Calendar c = Calendar.getInstance();
//		date = c.get(Calendar.DATE);
//		months = c.get(Calendar.MONTH);
//		years_now = c.get(Calendar.YEAR);
//
//		toDate = new StringBuilder().append(padding_str(years_now)).append("-")
//				.append(padding_str(months + 1)).append("-")
//				.append(padding_str(date));
//
//		edtDes.setText(dispatch.getDescription());
//		txtDateHandle.setText(dispatch.getDate());
//		txtDateFinish.setText(toDate);

	}

	// ------------choose date------------------//

	private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date = dayOfMonth;
			months = monthOfYear;
			years_now = year;

			txtDateHandle.setText(new StringBuilder()
					.append(padding_str(years_now)).append("-")
					.append(padding_str(months + 1)).append("-")
					.append(padding_str(date)));
		}

	};

	@Deprecated
	protected Dialog onCreateDialog(int id) {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				datePickerListener, years_now, months, date);
		datePickerDialog.getDatePicker().setSpinnersShown(false);
		datePickerDialog.getDatePicker().setCalendarViewShown(true);
		return datePickerDialog;
	}

	private static String padding_str(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
//			// clear data
//			listChecked.clear();
//			listCheckedHandler.clear();
//			DialogChoseHandler.strUsersHandl = getResources().getString(
//					R.string.handler1);
//			DialogChoseHandler.idUsersHandl = "";
//			DialogChoseUser.strUsersView = getResources().getString(
//					R.string.viewer);
//			DialogChoseUser.idUsersView = "";
			finish();
			break;
		case R.id.lnDateHandle:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnDateFinish:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnHandler:
			new DialogChoseHandler(AssignTaskActivity.this, listDep,
					allUser, listCheckedHandler);
			break;
		case R.id.lnViewer:

			ActionAdapter.flag = "chooseViewer";
			new DialogChoseUser(AssignTaskActivity.this, listDep, allUser,
					listChecked);

			break;
		case R.id.lnDepartment:
			new DialogChoseDepartment(AssignTaskActivity.this, listDep);
			break;
		case R.id.lnFile:
			Toast.makeText(getApplicationContext(), "chon file", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnProject:
			Toast.makeText(getApplicationContext(), "duan", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnAssign:
			Toast.makeText(getApplicationContext(), "Assign", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}