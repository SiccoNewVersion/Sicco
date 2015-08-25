package com.sicco.task.ultil;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.sicco.erp.R;
import com.sicco.erp.adapter.StatusAdapter;
import com.sicco.erp.manager.MyNotificationManager;
import com.sicco.erp.model.Status;
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

		status = new Status();

		if (task.getTrang_thai().equals("active")) {
			status.setKey(0);
		} else if (task.getTrang_thai().equals("inactive")) {
			status.setKey(1);
		} else if (task.getTrang_thai().equals("complete")) {
			status.setKey(2);
		} else if (task.getTrang_thai().equals("cancel")) {
			status.setKey(3);
		}

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

		if (task.getTrang_thai().equals("active")) {
			lvStatus.setItemChecked((int) status.getKey(), true);
		} else if (task.getTrang_thai().equals("inactive")) {
			lvStatus.setItemChecked((int) status.getKey(), true);
		} else if (task.getTrang_thai().equals("complete")) {
			lvStatus.setItemChecked((int) status.getKey(), true);
		} else if (task.getTrang_thai().equals("cancel")) {
			lvStatus.setItemChecked((int) status.getKey(), true);
		}

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
				progressDialog.setMessage(context.getResources().getString(
						R.string.waiting));
				task.changeStatusTask(context, task.getId(), status.getsKey(),
						new Task.OnLoadListener() {

							@Override
							public void onSuccess() {
								progressDialog.dismiss();
//								Toast.makeText(
//										context,
//										context.getResources().getString(
//												R.string.success),
//										Toast.LENGTH_LONG).show();
								MyNotificationManager mn = new MyNotificationManager();
								mn.notifyByState(context, task, (int)status.getKey());
								alertDialog.dismiss();

								task.setTrang_thai(status.getsKey());
								if(status.getsKey().equals("complete")){
									task.setTien_do("100");
								}
								
							}

							@Override
							public void onStart() {
								progressDialog.show();

							}

							@Override
							public void onFalse() {
								progressDialog.dismiss();
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.internet_false),
										Toast.LENGTH_SHORT).show();
							}
						});

			}
		});
	}
}