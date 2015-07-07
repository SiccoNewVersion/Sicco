package com.sicco.erp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.sicco.erp.adapter.TaskAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChoseDepartment;
import com.sicco.erp.util.DialogChoseUser;

public class ConvertDispatchActivity extends Activity implements
		OnClickListener {
	private ImageView back;
	private LinearLayout lnJobType, lnFromDate, lnStatus, lnProgress, lnLevel,
			lnHandler, lnViewer, lnDepartment, lnToDate;
	private TextView txtNumSignDispatch, txtJobType, txtFromDate, txtStatus,
			txtProgress, txtLevel, txtToDate;
	public static TextView txtDepartment;
	private EditText edtTitleJob;
	private Button btnConvert;

	public static TextView txtHandler, txtViewer;

	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked;
	private Dispatch dispatch;

	static final int DATE_DIALOG_ID = 111;
	private int date;
	private int months;
	private int years_now;

	private StringBuilder toDate;
	private Date d;
	String newDateString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_convert_dispatch);

		Intent intent = getIntent();
		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");

		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		lnJobType = (LinearLayout) findViewById(R.id.lnJobType);
		lnFromDate = (LinearLayout) findViewById(R.id.lnFromDate);
		lnStatus = (LinearLayout) findViewById(R.id.lnStatus);
		lnProgress = (LinearLayout) findViewById(R.id.lnProgress);
		lnLevel = (LinearLayout) findViewById(R.id.lnLevel);
		lnHandler = (LinearLayout) findViewById(R.id.lnHandler);
		lnViewer = (LinearLayout) findViewById(R.id.lnViewer);
		lnDepartment = (LinearLayout) findViewById(R.id.lnDepartment);
		lnToDate = (LinearLayout) findViewById(R.id.lnToDate);

		txtJobType = (TextView) findViewById(R.id.txtJobType);
		txtFromDate = (TextView) findViewById(R.id.txtFromDate);
		txtStatus = (TextView) findViewById(R.id.txtStatus);
		txtProgress = (TextView) findViewById(R.id.txtProgress);
		txtLevel = (TextView) findViewById(R.id.txtLevel);
		txtHandler = (TextView) findViewById(R.id.txtHandler);
		txtViewer = (TextView) findViewById(R.id.txtViewer);
		txtDepartment = (TextView) findViewById(R.id.txtDepartment);
		txtToDate = (TextView) findViewById(R.id.txtToDate);

		btnConvert = (Button) findViewById(R.id.btnConvert);

		edtTitleJob = (EditText) findViewById(R.id.edtTitle);

		// click
		back.setOnClickListener(this);

		lnJobType.setOnClickListener(this);
		lnFromDate.setOnClickListener(this);
		lnStatus.setOnClickListener(this);
		lnProgress.setOnClickListener(this);
		lnLevel.setOnClickListener(this);
		lnHandler.setOnClickListener(this);
		lnViewer.setOnClickListener(this);
		lnDepartment.setOnClickListener(this);
		lnToDate.setOnClickListener(this);
		btnConvert.setOnClickListener(this);

		listChecked = new ArrayList<User>();

		listDep = HomeActivity.listDep;
		allUser = HomeActivity.allUser;

		// set data
		final Calendar c = Calendar.getInstance();
		date = c.get(Calendar.DATE);
		months = c.get(Calendar.MONTH);
		years_now = c.get(Calendar.YEAR);
		
		toDate = new StringBuilder().append(padding_str(years_now))
				.append("-").append(padding_str(months + 1)).append("-")
				.append(padding_str(date));

		edtTitleJob.setText(getResources().getString(R.string.action)
				+ dispatch.getNumberDispatch());
		txtFromDate.setText(dispatch.getDate());
		txtToDate.setText(toDate);

	}

	// ------------choose date------------------//

	private DatePickerDialog.OnDateSetListener datePickerListener = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			date = dayOfMonth;
			months = monthOfYear;
			years_now = year;

			txtToDate.setText(new StringBuilder()
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

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.lnJobType:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.default_value),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnFromDate:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.default_value),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnStatus:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.default_value),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnProgress:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.default_value),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnLevel:
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.default_value),
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnHandler:

			TaskAdapter.flag = "chooseHandler";
			new DialogChoseUser(ConvertDispatchActivity.this, listDep, allUser,
					listChecked);

			break;
		case R.id.lnViewer:

			TaskAdapter.flag = "chooseViewer";
			new DialogChoseUser(ConvertDispatchActivity.this, listDep, allUser,
					listChecked);

			break;
		case R.id.lnDepartment:
			new DialogChoseDepartment(ConvertDispatchActivity.this, HomeActivity.listDep);
			break;
		case R.id.lnToDate:
			showDialog(DATE_DIALOG_ID);
			break;
		case R.id.btnConvert:
			Log.d("LuanDT", "userH: " + DialogChoseUser.strUsersHandl);
			Log.d("LuanDT", "userV: " + DialogChoseUser.strUsersView);
			Log.d("LuanDT", "id--userH: " + DialogChoseUser.idUsersHandl);
			Log.d("LuanDT", "id--userV: " + DialogChoseUser.idUsersView);
			Log.d("LuanDT", "id--idDepSelected: " + DialogChoseDepartment.idDepSelected);

			boolean error = false;
			if (edtTitleJob.getText().toString().trim().equals("")) {
				edtTitleJob.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			} else if (txtHandler.getText().toString()
					.equals(getResources().getString(R.string.handler1))) {
				txtHandler.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			} else if (txtViewer.getText().toString()
					.equals(getResources().getString(R.string.viewer))) {
				txtViewer.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			} else if (txtDepartment.getText().toString()
					.equals(getResources().getString(R.string.department))) {
				txtDepartment.setError(getResources().getString(
						R.string.truong_nay_khong_duoc_de_rong));
				error = true;
			}

			if (error == false) {

			}

			break;
		}
	}
}