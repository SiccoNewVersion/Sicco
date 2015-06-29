package com.sicco.erp;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ConvertDispatchActivity extends Activity implements
		OnClickListener {
	private ImageView back;
	private TextView title, cTitle, cJobType, cReceiveDate, cToDate, cProgress,
			cLever, cHandler, cViewer;
	private Spinner cDepartment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_convert_dispatch);
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		cTitle = (TextView) findViewById(R.id.c_title);
		cJobType = (TextView) findViewById(R.id.c_job_type);
		cReceiveDate = (TextView) findViewById(R.id.c_receive_date);
		cToDate = (TextView) findViewById(R.id.c_to_date);
		cProgress = (TextView) findViewById(R.id.c_progress);
		cLever = (TextView) findViewById(R.id.c_lever);
		cHandler = (TextView) findViewById(R.id.c_handler);
		cViewer = (TextView) findViewById(R.id.c_viewer);
		cDepartment = (Spinner) findViewById(R.id.c_department);
		// click
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		}
	}
}