package com.sicco.task.ultil;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
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
import com.sicco.erp.model.Status;
import com.sicco.task.erp.AssignedTaskActivity;
import com.sicco.task.erp.ListTask;
import com.sicco.task.model.Task;
import com.sicco.task.model.Task.OnLoadListener;

public class DialogSetProcess {
	private Context context;
	private StatusAdapter statusAdapter;
	private ArrayList<Status> listStatus;
	private TextView txtTitle;
	private Button btnDone;
	private Button btnRetry;
	private ListView lvStatus;
	private Task task;
	private Status status;
	
	int type;

	public DialogSetProcess(Context context,
			ArrayList<Status> listStatus, Task task) {
		super();
		this.context = context;
		this.listStatus = listStatus;
		this.task = task;
		status = new Status();
		status.setKey(Long.parseLong(task.getTien_do())/10);
	}

	@SuppressLint("InflateParams")
	public void showDialog() {
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
		btnRetry = (Button) layout.findViewById(R.id.retry);

		statusAdapter = new StatusAdapter(context, listStatus);
		lvStatus.setAdapter(statusAdapter);

		lvStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				status = (Status) parent.getAdapter().getItem(position);

			}
		});
		
		int selected = (int)status.getKey();
		Log.d("ToanNM", "task.getTien_do() : " + selected);
		lvStatus.setItemChecked(selected,
				true);
		Log.d("MyDebug", "selected is : " + selected);

		txtTitle.setText(context.getResources().getString(
				R.string.chose_process));

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

				task.changeProcess(
						context.getResources().getString(
								R.string.api_update_rate),
						Long.toString(task.getId()),
						status.getsKey(), new OnLoadListener() {

							@Override
							public void onStart() {
								progressDialog.show();
							}

							@Override
							public void onSuccess() {
								progressDialog.dismiss();
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.success),
										Toast.LENGTH_LONG).show();
								alertDialog.dismiss();
								
								// update ui
								if (AssignedTaskActivity.AssignedTaskActivity) {
									AssignedTaskActivity.arrTask = task
											.getData(
													context,
													context.getResources()
															.getString(
																	R.string.api_get_assigned_task),
													new OnLoadListener() {

														@Override
														public void onStart() {
															AssignedTaskActivity.loading
																	.setVisibility(View.VISIBLE);
															AssignedTaskActivity.connectError
																	.setVisibility(View.GONE);
														}

														@Override
														public void onSuccess() {
															AssignedTaskActivity.loading
																	.setVisibility(View.GONE);
															AssignedTaskActivity.adapter
																	.setData(AssignedTaskActivity.arrTask);
															AssignedTaskActivity.adapter
																	.notifyDataSetChanged();
															if (AssignedTaskActivity.adapter
																	.getCount() <= 0) {
																AssignedTaskActivity.listTask
																		.setEmptyView(AssignedTaskActivity.emptyView);
															}
														}

														@Override
														public void onFalse() {
															AssignedTaskActivity.loading
																	.setVisibility(View.GONE);
															AssignedTaskActivity.connectError
																	.setVisibility(View.VISIBLE);
														}
													});
								} else if (ListTask.ListTaskActivity) {
									ListTask.arrTask = task.getData(
											context,
											context.getResources().getString(
													R.string.api_get_task),
											new OnLoadListener() {

												@Override
												public void onStart() {
													ListTask.loading
															.setVisibility(View.VISIBLE);
													ListTask.connectError
															.setVisibility(View.GONE);
												}

												@Override
												public void onSuccess() {
													ListTask.loading
															.setVisibility(View.GONE);
													ListTask.adapter
															.setData(ListTask.arrTask);
													ListTask.adapter
															.notifyDataSetChanged();
													if (ListTask.adapter
															.getCount() <= 0) {
														ListTask.listTask
																.setEmptyView(ListTask.emptyView);
													}
												}

												@Override
												public void onFalse() {
													ListTask.loading
															.setVisibility(View.GONE);
													ListTask.connectError
															.setVisibility(View.VISIBLE);
												}
											});
								}
							}

							@Override
							public void onFalse() {

								progressDialog.dismiss();
								Toast.makeText(context, context.getResources().getString(R.string.internet_false), Toast.LENGTH_SHORT).show();
							}
						});

			}
		});
		// click retry
		btnRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Do something
			}
		});
	}

}