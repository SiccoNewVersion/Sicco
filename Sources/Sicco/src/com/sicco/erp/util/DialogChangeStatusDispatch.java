package com.sicco.erp.util;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.model.Status;
import com.sicco.erp.service.GetAllNotificationService;

public class DialogChangeStatusDispatch {
	private Context context;
	private StatusAdapter statusAdapter;
	private ArrayList<Status> listStatus;
	private TextView txtTitle;
	private Button btnDone;
	private Button btnRetry;
	private ListView lvStatus;
	private Dispatch dispatch;
	private Status status;
	
	int type;

	public DialogChangeStatusDispatch(Context context,
			ArrayList<Status> listStatus, Dispatch dispatch, int type) {
		super();
		this.context = context;
		this.listStatus = listStatus;
		this.dispatch = dispatch;
		this.type = type;
		status = new Status();
		status.setKey(Long.parseLong(dispatch.getStatus()));

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
		btnRetry = (Button) layout.findViewById(R.id.retry);

		statusAdapter = new StatusAdapter(context, listStatus);
		lvStatus.setAdapter(statusAdapter);

		lvStatus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				status = (Status) parent.getAdapter().getItem(position);

				Log.d("NgaDV", "status:" + status.getStatus());
			}
		});

		lvStatus.setItemChecked(Integer.parseInt(dispatch.getStatus()) - 1,
				true);

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

				dispatch.changeStatusDispatch(
						context.getResources().getString(
								R.string.api_change_status),
						Long.toString(dispatch.getId()),
						Long.toString(status.getKey()), new OnLoadListener() {

							@Override
							public void onStart() {
								progressDialog.show();
								progressDialog.setMessage(context
										.getResources().getString(
												R.string.waiting));
							}

							@Override
							public void onSuccess() {
								// ToanNM
								startGetAllNotificationService();
								setCount(type);
								// end of ToanNM
								progressDialog.dismiss();
								Toast.makeText(
										context,
										context.getResources().getString(
												R.string.success),
										Toast.LENGTH_LONG).show();
								alertDialog.dismiss();
								
								if (OtherActivity.otherActivitySelected) {
									OtherActivity.arrDispatch = dispatch
											.getData(context,
													context.getResources()
															.getString(
																	R.string.api_get_dispatch_other),
													new OnLoadListener() {

														@Override
														public void onStart() {
															OtherActivity.loading
																	.setVisibility(View.VISIBLE);
															OtherActivity.connectError
																	.setVisibility(View.GONE);
														}

														@Override
														public void onSuccess() {
															OtherActivity.loading
																	.setVisibility(View.GONE);
															OtherActivity.adapter
																	.setData(OtherActivity.arrDispatch);
															OtherActivity.adapter
																	.notifyDataSetChanged();
															if (OtherActivity.adapter
																	.getCount() <= 0) {
																OtherActivity.listDispatch
																		.setEmptyView(OtherActivity.emptyView);
															}
														}

														@Override
														public void onFalse() {
															OtherActivity.loading
																	.setVisibility(View.GONE);
															OtherActivity.connectError
																	.setVisibility(View.VISIBLE);
														}
													});
								} else {
									
									DealtWithActivity activity = new DealtWithActivity();
									
									DealtWithActivity.arrDispatch = dispatch
											.getData(context,
													context.getResources()
															.getString(
																	R.string.api_get_dispatch_handle),
													new OnLoadListener() {

														@Override
														public void onStart() {
															DealtWithActivity.loading
																	.setVisibility(View.VISIBLE);
															DealtWithActivity.connectError
																	.setVisibility(View.GONE);
														}

														@Override
														public void onSuccess() {
															DealtWithActivity.loading
																	.setVisibility(View.GONE);
															DealtWithActivity.adapter
																	.setData(DealtWithActivity.arrDispatch);
															DealtWithActivity.adapter
																	.notifyDataSetChanged();
															if (DealtWithActivity.adapter
																	.getCount() <= 0) {
																DealtWithActivity.listDispatch
																		.setEmptyView(DealtWithActivity.emptyView);
															}
														}

														@Override
														public void onFalse() {
															DealtWithActivity.loading
																	.setVisibility(View.GONE);
															DealtWithActivity.connectError
																	.setVisibility(View.VISIBLE);
														}
													});
								}
							}

							@Override
							public void onFalse() {

								progressDialog.dismiss();

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

	// ToanNM
	void startGetAllNotificationService() {
		Intent intent = new Intent(context, GetAllNotificationService.class);
		intent.putExtra("ACTION", 1);
		context.startService(intent);
	}

	void setCount(int type) {
		Log.d("ToanNM", "CongVanType : " + type);
		if (type == 1) {
			int count = Utils.getInt(context, GetAllNotificationService.CL_KEY);
			if (count != 0) {
				count--;
			} else if (count == 0) {
				cancelNotification(context, 3);
			}
			Utils.saveInt(context, GetAllNotificationService.CL_KEY, count);
		} else if (type == 0) {
			int count = Utils.getInt(context,
					GetAllNotificationService.CVXL_KEY);
			if (count != 0) {
				count--;
			} else if (count == 0) {
				cancelNotification(context, 2);
			}
			Utils.saveInt(context, GetAllNotificationService.CVXL_KEY, count);
		}
	}

	void cancelNotification(Context context, int notification_id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(notification_id);
	}
	// End of ToanNM

}
