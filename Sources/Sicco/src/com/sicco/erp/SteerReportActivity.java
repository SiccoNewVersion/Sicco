package com.sicco.erp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller.Session;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.ReportSteer.OnLoadListener;

public class SteerReportActivity extends Activity implements OnClickListener {

	private LinearLayout connectError;
	private ImageView back;
	private ListView listReport;
	private ProgressBar loading;
	private Button retry;
	private ImageView imgSendReportSteer;
	private ReportSteerAdapter reportSteerAdapter;
	private ArrayList<ReportSteer> arrReportSteers;
	private ReportSteer reportSteer;
	private EditText edtContent;
	private Dispatch dispatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steer_report);

		Intent intent = getIntent();
		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
		Log.d("NgaDV", "handler dispatch:"+dispatch.getHandler());
		init();
		setListReportSteer(dispatch);
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
		reportSteer = new ReportSteer(SteerReportActivity.this);
		edtContent = (EditText) findViewById(R.id.edtReportOrSteer);

		// click
		back.setOnClickListener(this);
		retry.setOnClickListener(this);
		// set adapter

	}

	private void sendReportSteer() {
		imgSendReportSteer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = new ProgressDialog(SteerReportActivity.this);
				
				String content = edtContent.getText().toString().trim();

				if (!content.equals("")) {
					reportSteer.sendReportSteer(getResources().getString(R.string.api_send_report), 
							SessionManager.KEY_USER_ID, 
							Long.toString(dispatch.getId()), 
							content, 
							new OnLoadListener() {
						
						@Override
						public void onSuccess() {
//							progressDialog.dismiss();
							Log.d("NgaDV", "onSuccess");
						}
						
						@Override
						public void onStart() {
//							progressDialog.setMessage("abc");
//							progressDialog.show();
							Log.d("NgaDV", "onStart");
						}
						
						@Override
						public void onFalse() {
							Log.d("NgaDV", "onFalse");
//							progressDialog.dismiss();
						}
					});

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

	private void setListReportSteer(Dispatch dispatch) {
		arrReportSteers = reportSteer.getData(
				getResources().getString(R.string.api_get_steer_report),
				SessionManager.KEY_USER_ID,
				Long.toString(dispatch.getId()),
				new OnLoadListener() {
					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						reportSteerAdapter.notifyDataSetChanged();
					}

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});

		reportSteerAdapter = new ReportSteerAdapter(getApplicationContext(),
				arrReportSteers);

		listReport.setAdapter(reportSteerAdapter);
	}

	private SessionManager SessionManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			arrReportSteers = reportSteer.getData(
					getResources().getString(R.string.api_get_steer_report),
					SessionManager.KEY_USER_ID,
					Long.toString(dispatch.getId()),
					new OnLoadListener() {

						@Override
						public void onSuccess() {
							loading.setVisibility(View.GONE);
							reportSteerAdapter.notifyDataSetChanged();
						}

						@Override
						public void onStart() {
							loading.setVisibility(View.VISIBLE);
							connectError.setVisibility(View.GONE);
						}

						@Override
						public void onFalse() {
							loading.setVisibility(View.GONE);
							connectError.setVisibility(View.VISIBLE);
						}
					});
			reportSteerAdapter = new ReportSteerAdapter(
					getApplicationContext(), arrReportSteers);

			listReport.setAdapter(reportSteerAdapter);
			reportSteerAdapter.notifyDataSetChanged();
			break;
		}

	}
}
