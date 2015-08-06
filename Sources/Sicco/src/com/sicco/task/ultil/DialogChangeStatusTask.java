package com.sicco.task.ultil;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.DealtWithActivity;
import com.sicco.erp.OtherActivity;
import com.sicco.erp.R;
import com.sicco.erp.adapter.StatusAdapter;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.Status;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.task.model.Task;

public class DialogChangeStatusTask {
	private Context context;
	private StatusAdapter statusAdapter;
	private ArrayList<Status> listStatus;
	private TextView txtTitle;
	private Button btnDone;
	private ListView lvStatus;
	private Status status;
	private Task task;

	public DialogChangeStatusTask(Context context,
			ArrayList<Status> listStatus, Task task) {
		super();
		this.context = context;
		this.listStatus = listStatus;
		this.task = task;

		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View layout = layoutInflater.inflate(
				R.layout.dialog_change_status_dispatch, null);
		layout.setMinimumWidth((int) (rect.width() * 1f));
		// layout.setMinimumHeight((int) (rect.height() * 1f));

		txtTitle = (TextView) layout.findViewById(R.id.title_actionbar);
		lvStatus = (ListView) layout.findViewById(R.id.lvStatus);
		btnDone = (Button) layout.findViewById(R.id.done);

		statusAdapter = new StatusAdapter(context, listStatus);
		lvStatus.setAdapter(statusAdapter);

		lvStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				status = (Status) parent.getAdapter().getItem(position);

			}
		});

//		lvStatus.setItemChecked( ,true);

		txtTitle.setText(context.getResources().getString(
				R.string.change_status));

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

				final ProgressDialog progressDialog = new ProgressDialog(
						context);
				progressDialog.setMessage(context
						.getResources().getString(
								R.string.waiting));
				
				
			}
		});
	}

}