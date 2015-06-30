package com.sicco.erp;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ConvertDispatchActivity extends Activity implements
		OnClickListener {
	private ImageView back;
	private LinearLayout lnTitle,lnJobType,lnFromDate,lnStatus,lnProgress,lnLevel,lnHandler,lnViewer,lnDepartment,lnToDate;
	private TextView txtNumSignDispatch,txtJobType,txtFromDate,txtStatus,txtProgress,txtLevel,txtHandler,txtViewer,txtDepartment,txtToDate;
	private EditText edtTitleJob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_convert_dispatch);
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		lnTitle = (LinearLayout)findViewById(R.id.lnTitle);
		lnJobType = (LinearLayout)findViewById(R.id.lnJobType);
		lnFromDate = (LinearLayout)findViewById(R.id.lnFromDate);
		lnStatus = (LinearLayout)findViewById(R.id.lnStatus);
		lnProgress = (LinearLayout)findViewById(R.id.lnProgress);
		lnLevel = (LinearLayout)findViewById(R.id.lnLevel);
		lnHandler = (LinearLayout)findViewById(R.id.lnHandler);
		lnViewer = (LinearLayout)findViewById(R.id.lnViewer);
		lnDepartment = (LinearLayout)findViewById(R.id.lnDepartment);
		lnToDate = (LinearLayout)findViewById(R.id.lnToDate);
		
		txtNumSignDispatch = (TextView)findViewById(R.id.txtNumberSignDispatch);
		txtJobType = (TextView)findViewById(R.id.txtJobType);
		txtFromDate = (TextView)findViewById(R.id.txtFromDate);
		txtStatus = (TextView)findViewById(R.id.txtStatus);
		txtProgress = (TextView)findViewById(R.id.txtProgress);
		txtLevel = (TextView)findViewById(R.id.txtLevel);
		txtHandler = (TextView)findViewById(R.id.txtHandler);
		txtViewer = (TextView)findViewById(R.id.txtViewer);
		txtDepartment = (TextView)findViewById(R.id.txtDepartment);
		txtToDate = (TextView)findViewById(R.id.txtToDate);
		
		edtTitleJob = (EditText)findViewById(R.id.edtTitle);
		
		// click
		back.setOnClickListener(this);
		
		lnTitle.setOnClickListener(this);
		lnJobType.setOnClickListener(this);
		lnFromDate.setOnClickListener(this);
		lnStatus.setOnClickListener(this);
		lnProgress.setOnClickListener(this);
		lnLevel.setOnClickListener(this);
		lnHandler.setOnClickListener(this);
		lnViewer.setOnClickListener(this);
		lnDepartment.setOnClickListener(this);
		lnToDate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.lnTitle:
			Toast.makeText(getApplicationContext(), "lnTitle", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnJobType:
			Toast.makeText(getApplicationContext(), "lnJobType", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnFromDate:
			Toast.makeText(getApplicationContext(), "lnFromDate", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnStatus:
			Toast.makeText(getApplicationContext(), "lnStatus", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnProgress:
			Toast.makeText(getApplicationContext(), "lnProgress", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnLevel:
			Toast.makeText(getApplicationContext(), "lnLevel", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnHandler:
			Toast.makeText(getApplicationContext(), "lnHandler", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnViewer:
			Toast.makeText(getApplicationContext(), "lnViewer", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnDepartment:
			Toast.makeText(getApplicationContext(), "lnDepartment", Toast.LENGTH_SHORT).show();
			break;
		case R.id.lnToDate:
			Toast.makeText(getApplicationContext(), "lnToDate", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}