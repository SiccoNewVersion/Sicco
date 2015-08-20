package com.sicco.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.DetailDispatchActivity;
import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.Utils;
import com.sicco.task.erp.DetailTaskActivity;
import com.sicco.task.erp.SteerReportTaskActivity;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;
import com.sicco.task.ultil.DialogConfirmDeleteTask;
import com.sicco.task.ultil.DialogSetProcess;

public class TaskAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Task> data;
	private int type;
//	private ArrayList<Status> listStatus;
//	private ArrayList<Status> listProcess;
	NotificationDBController db;
	Cursor cursor;
//	boolean update_status_and_rate = false;

	public TaskAdapter(Context context, ArrayList<Task> data, int type) {
		super();
		this.context = context;
		this.data = data;
		this.type = type;
		
//		listStatus = new ArrayList<Status>();
//
//		listStatus.add(new Status(0, "active", context.getResources().getString(R.string.active)));
//		listStatus.add(new Status(1, "inactive", context.getResources().getString(R.string.inactive)));
//		listStatus.add(new Status(2, "complete", context.getResources().getString(R.string.complete)));
//		listStatus.add(new Status(3, "cancel", context.getResources().getString(R.string.cancel)));
		
	}

	public ArrayList<Task> getData() {
		return data;
	}

	public void setData(ArrayList<Task> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Task getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		final ViewHolder holder;
		final Task task = getItem(arg0);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_task,
					arg2, false);
			holder = new ViewHolder();
			holder.taskName = (TextView) view.findViewById(R.id.taskName);
			holder.handler = (TextView) view.findViewById(R.id.handler);
			holder.date_handle = (TextView) view.findViewById(R.id.date_handle);
			holder.date_finish = (TextView) view.findViewById(R.id.date_finish);
			holder.action = (TextView) view.findViewById(R.id.action);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		if (type == 2) {
			long id = task.getId();
			String state = querryFromDB(context, id);
			if (state
					.equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)) {
				view.setBackgroundColor(context.getResources().getColor(
						R.color.item_color));
			} else {
				view.setBackgroundColor(Color.WHITE);
			}
		}

		if (task.getTrang_thai().equals("complete")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.red));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.red));
			holder.date_handle.setTextColor(context.getResources().getColor(
					R.color.red));
			holder.date_finish.setTextColor(context.getResources().getColor(
					R.color.red));
		} else if (task.getTrang_thai().equals("cancel")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.yellow));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.yellow));
			holder.date_handle.setTextColor(context.getResources().getColor(
					R.color.yellow));
			holder.date_finish.setTextColor(context.getResources().getColor(
					R.color.yellow));
		} else if (task.getTrang_thai().equals("inactive")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.date_handle.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.date_finish.setTextColor(context.getResources().getColor(
					R.color.gray));
		} else if (task.getTrang_thai().equals("active")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.green));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.green));
			holder.date_handle.setTextColor(context.getResources().getColor(
					R.color.green));
			holder.date_finish.setTextColor(context.getResources().getColor(
					R.color.green));
		}

		String date_handle_no_time = task.getNgay_bat_dau().substring(0,10);
		String date_finish_no_time = task.getNgay_ket_thuc().substring(0,10);
		String handler = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.nguoi_thuc_hien)
				+ "</i></u></b></font>" + "  " + task.getNguoi_thuc_hien();
		String date_handle = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.date_handle)
				+ "</i></u></b></font>" + ":  " + date_handle_no_time;
		String date_finish = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.date_finish)
				+ "</i></u></b></font>" + ":  " + date_finish_no_time;

		holder.taskName.setText(task.getTen_cong_viec());
		holder.handler.setText(Html.fromHtml(handler));
		holder.date_handle.setText(Html.fromHtml(date_handle));
		holder.date_finish.setText(Html.fromHtml(date_finish));

		holder.action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				update_status_and_rate = false;
//				final String[] nguoithuchien = task.getNguoi_thuc_hien().split(
//						",");
//				final String username = Utils.getString(context, "name");
//				for (int i = 0; i < nguoithuchien.length; i++) {
//					if (username.equals(nguoithuchien[i])) {
//						update_status_and_rate = true;
//					}
//				}

				PopupMenu popupMenu = new PopupMenu(context, holder.action);
				if (type == 1) {
					popupMenu.getMenuInflater().inflate(R.menu.assigned_task,
							popupMenu.getMenu());
				} else {
					popupMenu.getMenuInflater()
							.inflate(R.menu.assigned_task1,
									popupMenu.getMenu());
				}

				popupMenu.show();
				popupMenu
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								Intent intent = new Intent();
								switch (item.getItemId()) {
								case R.id.action_report:
									intent.setClass(context,
											SteerReportTaskActivity.class);
									intent.putExtra("task", task);
									context.startActivity(intent);
									break;
//								case R.id.action_update_rate:
//									listProcess = new ArrayList<Status>();
//
//									String[] key = context
//											.getResources()
//											.getStringArray(R.array.process_key);
//									String[] value = context.getResources()
//											.getStringArray(
//													R.array.process_value);
//									int max = key.length;
//									for (int i = 0; i < max; i++) {
//										listProcess.add(new Status(i + 1,
//												key[i], value[i]));
//									}
//									if (type == 2) {
//										if (update_status_and_rate) {
//											DialogSetProcess dialog = new DialogSetProcess(
//													context, listProcess, task);
//											dialog.showDialog();
//										} else {
//											Toast.makeText(
//													context,
//													context.getResources()
//															.getString(
//																	R.string.info_update_rate),
//													Toast.LENGTH_SHORT).show();
//										}
//									} else {
//										DialogSetProcess dialog = new DialogSetProcess(
//												context, listProcess, task);
//										dialog.showDialog();
//									}
//									break;
//								case R.id.action_change_status:
//									if (type == 2) {
//										if (update_status_and_rate) {
//											new DialogChangeStatusTask(context, listStatus, task);
//										} else {
//											Toast.makeText(
//													context,
//													context.getResources()
//															.getString(
//																	R.string.info_update_status),
//													Toast.LENGTH_SHORT).show();
//										}
//									} else {
//										new DialogChangeStatusTask(context, listStatus, task);
//									}
//									break;
								case R.id.action_detail:
									intent.setClass(context,
											DetailTaskActivity.class);
									intent.putExtra("task", task);
									context.startActivity(intent);
									break;
								case R.id.action_delete:
									new DialogConfirmDeleteTask(context, task);
									break;

								}
								return false;
							}
						});
			}
		});

		return view;
	}

	private class ViewHolder {
		private TextView taskName;
		private TextView handler;
		private TextView date_handle;
		private TextView date_finish;
		private TextView action;
	}
	
	String querryFromDB(Context context, long position) {
		String state = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.ID_COL + " = " + position;
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				state = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TRANGTHAI_COL));
			} while (cursor.moveToNext());
		}
		return state;
	}

}
