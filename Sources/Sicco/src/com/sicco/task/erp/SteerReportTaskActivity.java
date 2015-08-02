package com.sicco.task.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.ReportSteerTaskAdapter;
import com.sicco.task.model.ReportSteerTask;

public class SteerReportTaskActivity extends Activity implements
		OnClickListener, OnItemClickListener {

	private LinearLayout connectError;
	private ImageView back;
	private ListView listReport;
	private ProgressBar loading;
	private Button retry;
	private ImageView imgSendReportSteer;
	private ReportSteerTaskAdapter adapter;
	private ArrayList<ReportSteerTask> arrReportSteers;
	private ReportSteerTask reportSteerTask;
	private EditText edtContent;
	private TextView emptyView;
	private String id_task;
	private ViewDispatch viewDispatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steer_report_task);

		Intent intent = getIntent();
		id_task = intent.getStringExtra("id_task");
		init();
		setListReportSteer(id_task);
		sendReportSteer();

	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		imgSendReportSteer = (ImageView) findViewById(R.id.imgSendReportSteer);
		listReport = (ListView) findViewById(R.id.listReport);
		imgSendReportSteer = (ImageView) findViewById(R.id.imgSendReportSteer);
		edtContent = (EditText) findViewById(R.id.edtReportOrSteer);
		emptyView = (TextView) findViewById(R.id.empty_view);
		reportSteerTask = new ReportSteerTask(SteerReportTaskActivity.this);

		// click
		back.setOnClickListener(this);
		retry.setOnClickListener(this);
		listReport.setOnItemClickListener(this);

	}

	private void sendReportSteer() {
		imgSendReportSteer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = new ProgressDialog(
						SteerReportTaskActivity.this);

				String content = edtContent.getText().toString().trim();

				if (!content.equals("")) {

				} else {
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(
									R.string.empty_content_report),
							Toast.LENGTH_SHORT).show();
					edtContent.startAnimation(AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.shake));
				}

			}
		});
	}

	private void setListReportSteer(String id_task) {
		arrReportSteers = new ArrayList<ReportSteerTask>();
		arrReportSteers.add(new ReportSteerTask(1, "handler", "date", "content", "file"));
//		arrReportSteers = reportSteerTask.getData(SteerReportTaskActivity.this,
//				getResources().getString(R.string.api_get_steer_report_task),
//				id_task, new ReportSteerTask.OnLoadListener() {
//					@Override
//					public void onSuccess() {
//						loading.setVisibility(View.GONE);
//						adapter.notifyDataSetChanged();
//
//						if (adapter.getCount() <= 0) {
//							listReport.setEmptyView(emptyView);
//						}
//
//					}
//
//					@Override
//					public void onStart() {
//						loading.setVisibility(View.VISIBLE);
//						connectError.setVisibility(View.GONE);
//					}
//
//					@Override
//					public void onFalse() {
//						loading.setVisibility(View.GONE);
//						connectError.setVisibility(View.VISIBLE);
//					}
//				});

		adapter = new ReportSteerTaskAdapter(SteerReportTaskActivity.this, arrReportSteers);
		listReport.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			setListReportSteer(id_task);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ReportSteerTask reportSteerTask = (ReportSteerTask) arg0.getAdapter().getItem(arg2);
		viewDispatch = new ViewDispatch(SteerReportTaskActivity.this,
				reportSteerTask.getFile());
	}
}