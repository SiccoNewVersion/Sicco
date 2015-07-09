package com.sicco.erp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.adapter.SpinnerStatusAdapter;
import com.sicco.erp.manager.SessionManager;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.Status;

public class ChangeStatusDispatch extends Activity implements OnClickListener {
	
	private Spinner spnStatusDispatch;
	private SpinnerStatusAdapter spinnerStatusAdapter;
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
	private String arr[] ;
	private ArrayList<Status> listStatus;
	
	Dispatch dispatch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_status_dispatch);
		Intent intent = getIntent();
		dispatch = (Dispatch) intent.getSerializableExtra("dispatch");
		init();
		setListReportSteer(dispatch);
		sendReportSteer();
		setSpinnerStatus();
	}
	private void init() {
		
			listStatus = new ArrayList<Status>();
			back 				= 	(ImageView)		findViewById(R.id.back);
			loading 			= 	(ProgressBar) 	findViewById(R.id.loading);
			retry 				= 	(Button) 		findViewById(R.id.retry);
			connectError 		= 	(LinearLayout) 	findViewById(R.id.connect_error);
			imgSendReportSteer 	= 	(ImageView)		findViewById(R.id.imgSendReportSteer);
			listReport 			= 	(ListView) 		findViewById(R.id.listReport);
			imgSendReportSteer 	= 	(ImageView) 	findViewById(R.id.imgSendReportSteer);
			reportSteer 		= 	new 			ReportSteer(getApplicationContext());
			edtContent 			= 	(EditText)		findViewById(R.id.edtReportOrSteer);
			spnStatusDispatch	= 	(Spinner)		findViewById(R.id.spnStatusDispatch);

			// click
			back.setOnClickListener(this);
			retry.setOnClickListener(this);
			 

		}
	
	private void setSpinnerStatus(){
		
		for (int i = 0; i < arr.length; i++) {
//			listStatus.add(new Status(arr[i], Integer.toString(i)));
		}
		spinnerStatusAdapter= new SpinnerStatusAdapter(ChangeStatusDispatch.this, listStatus);
		
		 
		 spnStatusDispatch.setAdapter(spinnerStatusAdapter);
		 
	}
		private void sendReportSteer() {
			imgSendReportSteer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					String content = edtContent.getText().toString().trim();
					
					if (!content.equals("")) {
						edtContent.setText("");//reset edtContent
						arrReportSteers.add(new ReportSteer(1,content,new Date().toString(),content));
						reportSteerAdapter = new ReportSteerAdapter(getApplicationContext(), arrReportSteers);
						listReport.setAdapter(reportSteerAdapter);
						reportSteerAdapter.notifyDataSetChanged();
						listReport.setSelection(arrReportSteers.size()-1);
						
					}else {
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_content_report), Toast.LENGTH_SHORT).show();
						edtContent.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake));
					}
					
				}
			});
		}

		private void setListReportSteer(Dispatch dispatch) {

			arrReportSteers = reportSteer.getData(
					getResources().getString(R.string.api_get_steer_report),
					SessionManager.KEY_USER_ID,
					Long.toString(dispatch.getId()),
					new ReportSteer.OnLoadListener() {

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
						new ReportSteer.OnLoadListener() {

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
				break;
			}

		}
}
