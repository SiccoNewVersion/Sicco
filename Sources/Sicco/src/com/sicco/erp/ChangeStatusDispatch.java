package com.sicco.erp;

import java.util.ArrayList;
import java.util.Date;

import com.sicco.erp.adapter.ReportSteerAdapter;
import com.sicco.erp.model.ReportSteer;
import com.sicco.erp.model.Dispatch.OnLoadListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeStatusDispatch extends Activity implements OnClickListener {
	
	private Spinner spnStatusDispatch;
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
	private TextView txtStatusDispatch;
	private String arr[] = {
			 "Chá»�n tráº¡ng thĂ¡i",
			 "Cáº§n phĂª",
			 "Cáº§n xá»­ lĂ½",
			 "Táº¡m dá»«ng xá»­ lĂ½",
			 "Káº¿t thĂºc xá»­ lĂ½"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_status_dispatch);
		init();
		setListReportSteer();
		sendReportSteer();
		setSpinnerStatus();
	}
	private void init() {
			back 				= 	(ImageView)		findViewById(R.id.back);
			loading 			= 	(ProgressBar) 	findViewById(R.id.loading);
			retry 				= 	(Button) 		findViewById(R.id.retry);
			connectError 		= 	(LinearLayout) 	findViewById(R.id.connect_error);
			imgSendReportSteer 	= 	(ImageView)		findViewById(R.id.imgSendReportSteer);
			listReport 			= 	(ListView) 		findViewById(R.id.listReport);
			imgSendReportSteer 	= 	(ImageView) 	findViewById(R.id.imgSendReportSteer);
			reportSteer 		= 	new ReportSteer(getApplicationContext());
			edtContent 			= 	(EditText)		findViewById(R.id.edtReportOrSteer);
			spnStatusDispatch	= 	(Spinner)		findViewById(R.id.spnStatusDispatch);
			txtStatusDispatch	=	(EditText)		findViewById(R.id.txtStatusDispatch);

			// click
			back.setOnClickListener(this);
			retry.setOnClickListener(this);
			 

		}
	
	private void setSpinnerStatus(){
		 
		 ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
		 adapter.setDropDownViewResource
		 (android.R.layout.simple_dropdown_item_1line);
		 spnStatusDispatch.setAdapter(adapter);
		 
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

		private void setListReportSteer() {

			// arrReportSteers = new ArrayList<ReportSteer>();
			//
			// for (int i = 0; i < 10; i++) {
			// arrReportSteers.add(new ReportSteer(i, "NgaDV"+i, i+"-02-201"+i,
			// "content "+i));
			//
			// Log.d("NgaDV", arrReportSteers.get(i).getHandler());
			// }

			arrReportSteers = reportSteer.getData(
					"http://myapp.freezoy.com/reportsteer.php",
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
						"http://myapp.freezoy.com/reportsteer.php",
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
