package com.sicco.task.erp;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.adapter.ActionAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Project;
import com.sicco.erp.model.User;
import com.sicco.erp.util.ChooseFileActivity;
import com.sicco.erp.util.DialogChooseDepartment;
import com.sicco.erp.util.DialogChooseHandler;
import com.sicco.erp.util.DialogChooseProject;
import com.sicco.erp.util.DialogChooseUser;

public class AssignTaskActivity extends ChooseFileActivity implements
		OnClickListener {
	private static int FLAG_DATE_PICKER = 1;
	private ImageView back;
	private ScrollView scrollView;
	private LinearLayout 	lnDateHandle,
							lnHandler, 
							lnViewer, 
							lnDepartment, 
							lnDateFinish,
							lnFile,
							lnProject,
							lnTitle,
							lnDescription;
	
	private TextView 	txtDateHandle;
	private TextView 	txtDateFinish;
	public static TextView txtDepartment, txtHandler, txtViewer,txtProject;
	private EditText 	edtDes;
	private EditText 	edtTitle;
	private Button 		btnAssign;
	private TextView 	txtFile;
	
	private ArrayList<Department> listDep;
	private ArrayList<Project> listProject;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked, listCheckedHandler;
	private Dispatch dispatch;

	static final int DATE_DIALOG_ID = 111;
	private int date;
	private int months;
	private int years_now;

	private StringBuilder toDate;
	private Department department;
	private Project project;
	private User user;
	private String file;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assign_task);
		
		DialogChooseHandler.VIEW_CURRENT = 1;
//
//		Intent intent = getIntent();
//		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
//		file = dispatch.getContent();
		init();
	}

	private void init() {
		back 			= (ImageView) 		findViewById(R.id.back);
		
		scrollView		= (ScrollView)		findViewById(R.id.scrMain);
		lnTitle			= (LinearLayout)	findViewById(R.id.lnTitle);
		lnDescription	= (LinearLayout)	findViewById(R.id.lndes);
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

		listChecked = new ArrayList<User>();
		listCheckedHandler = new ArrayList<User>();

		department 	= new Department();
		project 	= new Project();
		user 		= new User();
		listDep 	= new ArrayList<Department>();
		listProject = new ArrayList<Project>();
		allUser 	= new ArrayList<User>();
		
		listDep 	= department.getData(getResources().getString(
									R.string.api_get_deparment));
		listProject = project.getData(getResources().getString(R.string.api_get_project));
		allUser 	= user.getData(getResources().getString(
				R.string.api_get_all_user));

		// set data
		final Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DATE);
		months = c.get(Calendar.MONTH);
		years_now = c.get(Calendar.YEAR);
//
		toDate = new StringBuilder().append(padding_str(years_now)).append("-")
				.append(padding_str(months + 1)).append("-")
				.append(padding_str(date));
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

			if(FLAG_DATE_PICKER == 1){
				txtDateHandle.setText(new StringBuilder()
					.append(padding_str(years_now)).append("-")
					.append(padding_str(months + 1)).append("-")
					.append(padding_str(date)));
			}else {
				txtDateFinish.setText(new StringBuilder()
				.append(padding_str(years_now)).append("-")
				.append(padding_str(months + 1)).append("-")
				.append(padding_str(date)));
			}
			
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
			// clear data
			listChecked.clear();
			listCheckedHandler.clear();
			DialogChooseHandler.strUsersHandl = getResources().getString(
					R.string.handler1);
			DialogChooseHandler.idUsersHandl = "";
			DialogChooseUser.strUsersView = getResources().getString(
					R.string.viewer);
			DialogChooseUser.idUsersView = "";
			finish();
			break;
		case R.id.lnDateHandle:
			FLAG_DATE_PICKER = 1;
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnDateFinish:
			FLAG_DATE_PICKER = 0;
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.lnHandler:
			new DialogChooseHandler(AssignTaskActivity.this, listDep,
					allUser, listCheckedHandler);
			break;
		case R.id.lnViewer:

			ActionAdapter.flag = "chooseViewer";
			new DialogChooseUser(AssignTaskActivity.this, listDep, allUser,
					listChecked);
			break;
		case R.id.lnDepartment:
			new DialogChooseDepartment(AssignTaskActivity.this, listDep);
			break;
		case R.id.lnFile:
			showFileChooser(); 
			break;
		case R.id.lnProject:
			new DialogChooseProject(AssignTaskActivity.this, listProject);
			Toast.makeText(getApplicationContext(), "dua", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btnAssign:
			validatFromAssignTask();
			break;
		}
	}
	private boolean validatFromAssignTask() {
		boolean validate = true;
		String title = edtTitle.getText().toString().trim();
		String description = edtDes.getText().toString().trim();
		String date_handle = txtDateHandle.getText().toString().trim();
		String date_finish = txtDateFinish.getText().toString().trim();
		String handler = txtHandler.getText().toString().trim();
		String viewer = txtViewer.getText().toString().trim();
		String department = txtDepartment.getText().toString().trim();
		
		if (title.equals("")) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.title_null), Toast.LENGTH_SHORT).show();
			edtTitle.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			edtTitle.requestFocus();
			scrollView.setScrollY((int)lnTitle.getY());
			validate = false;
		}else if (description.equals("")) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.description_null), Toast.LENGTH_SHORT).show();
			edtDes.requestFocus();
			edtDes.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int)lnDescription.getY());
			validate = false;
		}else if (date_handle.equals(getResources().getString(R.string.date_handle))) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.date_handle_null), Toast.LENGTH_SHORT).show();
			lnDateHandle.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int)lnDateHandle.getY());
			validate = false;
		}else if (date_finish.equals(getResources().getString(R.string.date_finish))) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.date_finish_null), Toast.LENGTH_SHORT).show();
			lnDateFinish.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int)lnDateFinish.getY());
			validate = false;
		}else if (handler.equals(getResources().getString(R.string.handler1))) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.handler_null), Toast.LENGTH_SHORT).show();
			lnHandler.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int)lnHandler.getY());
			validate = false;
		}else if (viewer.equals(getResources().getString(R.string.viewer))) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.viewer_null), Toast.LENGTH_SHORT).show();
			lnViewer.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int)lnViewer.getY());
			validate = false;
		}else if (department.equals(getResources().getString(R.string.department))) {
			Toast.makeText(AssignTaskActivity.this, getResources().getString(R.string.department_null), Toast.LENGTH_SHORT).show();
			lnDepartment.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
			scrollView.setScrollY((int)lnDepartment.getY());
			validate = false;
		}else {
			validate = true;
		}
		
		return validate;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				path = uri.getPath();
				txtFile.setText(getResources().getString(R.string.path_file) + path);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	protected void onResume() {
		Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
		super.onResume();
	}
}