package com.sicco.erp;

import java.util.ArrayList;

import com.sicco.erp.adapter.DispatchAdapter;
import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.ReportSteer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steer_report);
//		
//		
//		connectError = (LinearLayout) findViewById(R.id.connect_error);
//		loading = (ProgressBar) findViewById(R.id.loading);
//		retry = (Button) findViewById(R.id.retry);
//		imgSendReportSteer = (ImageView)findViewById(R.id.imgSendReportSteer);
//		listReport = (ListView)findViewById(R.id.listReport);
//		reportSteer = new ReportSteer(this);
		
//		arrReportSteers = new ArrayList<ReportSteer>();
//		arrReportSteers.add(new ReportSteer(1, "ngadv", "01-01-1010", "content"));
//		
//		Log.d("NgaDV", ""+arrReportSteers.get(0).getHandler());
//		arrReportSteers = reportSteer.getData("http://myapp.freezoy.com/reportsteer.php", new OnLoadListener() {
//			
//			@Override
//			public void onSuccess() {
//				loading.setVisibility(View.GONE);
//				reportSteerAdapter.notifyDataSetChanged();
//			}
//			
//			@Override
//			public void onStart() {
//				loading.setVisibility(View.VISIBLE);
//				connectError.setVisibility(View.GONE);
//			}
//			
//			@Override
//			public void onFalse() {
//				loading.setVisibility(View.GONE);
//				connectError.setVisibility(View.VISIBLE);
//			}
//		});
		
		
//		reportSteerAdapter = new ReportSteerAdapter(getApplicationContext(), arrReportSteers);
//		listReport.setAdapter(reportSteerAdapter);
//		
//		
//		
//		imgSendReportSteer.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "Cliked", Toast.LENGTH_SHORT).show();
//			}
//		});
		
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		Toast.makeText(SteerReportActivity.this, id, Toast.LENGTH_SHORT).show();
		init();
	}
	
	private void init() {
		back = (ImageView) findViewById(R.id.back);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		
		listReport = (ListView) findViewById(R.id.listReport);
		imgSendReportSteer = (ImageView)findViewById(R.id.imgSendReportSteer);
		reportSteer = new ReportSteer(getApplicationContext());
//		arrReportSteers = new ArrayList<ReportSteer>();
//		
//		for (int i = 0; i < 10; i++) {
//			arrReportSteers.add(new ReportSteer(i, "NgaDV"+i, i+"-02-201"+i, "content "+i));
//			
//			Log.d("NgaDV", arrReportSteers.get(i).getHandler());
//		}
		
		arrReportSteers = reportSteer.getData("http://myapp.freezoy.com/reportsteer.php", new OnLoadListener() {
			
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
		
		Log.d("NgaDV", "arrReportSteers.size() = "+arrReportSteers.size());
		reportSteerAdapter = new ReportSteerAdapter(getApplicationContext(), arrReportSteers);
		
		listReport.setAdapter(reportSteerAdapter);
		// click
		back.setOnClickListener(this);
		retry.setOnClickListener(this);
		// set adapter
		
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			
			break;
		}
		
	}
}
