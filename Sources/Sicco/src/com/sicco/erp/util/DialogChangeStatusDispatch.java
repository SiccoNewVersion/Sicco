package com.sicco.erp.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.adapter.StatusAdapter;
import com.sicco.erp.model.Status;

public class DialogChangeStatusDispatch {
	private Context context;
	private StatusAdapter statusAdapter;
	private ArrayList<Status> listStatus;
	private TextView txtTitle;
	private RadioButton rdStatus;
	private Button btnDone;
	private Button btnRetry;
	private ListView lvStatus;
	
	public DialogChangeStatusDispatch(Context context,
			ArrayList<Status> listStatus) {
		super();
		this.context = context;
		this.listStatus = listStatus;
		
		Log.d("NgaDV", "listStatus.size():"+listStatus.size());
		
		showDialog();
	}
	
	private void showDialog(){
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View layout = layoutInflater.inflate(R.layout.dialog_change_status_dispatch, null);
		layout.setMinimumWidth((int) (rect.width() * 1f));
//		layout.setMinimumHeight((int) (rect.height() * 1f));
		
		txtTitle = (TextView)layout.findViewById(R.id.title_actionbar);
		lvStatus = (ListView)layout.findViewById(R.id.lvStatus);
		btnDone = (Button)layout.findViewById(R.id.done);
		btnRetry = (Button)layout.findViewById(R.id.retry);
		
//		listStatus = new ArrayList<Status>();
//		listStatus.add(new Status("st1", "1"));
//		listStatus.add(new Status("st2", "2"));
//		listStatus.add(new Status("st3", "3"));
//		listStatus.add(new Status("st4", "4"));
		
		statusAdapter = new StatusAdapter(context, R.layout.item_status, listStatus);
		lvStatus.setAdapter(statusAdapter);
		
		
		txtTitle.setText("abc");
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(layout);
		builder.setCancelable(true);

		final AlertDialog alertDialog = builder.show();
		ImageView imgBack = (ImageView) layout.findViewById(R.id.back);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alertDialog.dismiss();

			}
		});

		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// do something
			}
		});
		// click retry
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Do something
			}
		});
	}
	
}
