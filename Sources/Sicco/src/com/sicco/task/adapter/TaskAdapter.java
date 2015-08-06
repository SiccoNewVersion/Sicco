package com.sicco.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
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

import com.sicco.erp.R;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.Utils;
import com.sicco.task.erp.SteerReportTaskActivity;
import com.sicco.task.model.Task;
import com.sicco.task.ultil.DialogChangeStatusTask;

public class TaskAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Task> data;
	private int type;
	private boolean update_status_and_rate = false;
	private ArrayList<Status> listStatus;

	public TaskAdapter(Context context, ArrayList<Task> data, int type) {
		super();
		this.context = context;
		this.data = data;
		this.type = type;
		
		listStatus = new ArrayList<Status>();

		listStatus.add(new Status(1, "active", context.getResources().getString(R.string.active)));
		listStatus.add(new Status(2, "inactive", context.getResources().getString(R.string.inactive)));
		listStatus.add(new Status(3, "complete", context.getResources().getString(R.string.complete)));
		listStatus.add(new Status(4, "cancel", context.getResources().getString(R.string.cancel)));
		
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
			holder.project = (TextView) view.findViewById(R.id.project);
			holder.action = (TextView) view.findViewById(R.id.action);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (task.getTrang_thai().equals("complete")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.red));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.red));
			holder.project.setTextColor(context.getResources().getColor(
					R.color.red));
		} else if (task.getTrang_thai().equals("cancel")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.yellow));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.yellow));
			holder.project.setTextColor(context.getResources().getColor(
					R.color.yellow));
		} else if (task.getTrang_thai().equals("inactive")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.gray));
			holder.project.setTextColor(context.getResources().getColor(
					R.color.gray));
		} else if (task.getTrang_thai().equals("active")) {
			holder.taskName.setTextColor(context.getResources().getColor(
					R.color.green));
			holder.handler.setTextColor(context.getResources().getColor(
					R.color.green));
			holder.project.setTextColor(context.getResources().getColor(
					R.color.green));
		}

		String handler = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.nguoi_thuc_hien)
				+ "</i></u></b></font>" + "  " + task.getNguoi_thuc_hien();
		String project = "<font weigth='bold'><b><u><i>"
				+ context.getResources().getString(R.string.du_an)
				+ "</i></u></b></font>" + "  " + task.getDu_an();

		holder.taskName.setText(task.getTen_cong_viec());
		holder.handler.setText(Html.fromHtml(handler));
		holder.project.setText(Html.fromHtml(project));

		holder.action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String[] nguoithuchien = task.getNguoi_thuc_hien().split(
						",");
				final String username = Utils.getString(context, "name");
				for (int i = 0; i < nguoithuchien.length; i++) {
					if (username.equals(nguoithuchien[i])) {
						update_status_and_rate = true;
					}
				}

				PopupMenu popupMenu = new PopupMenu(context, holder.action);
				if (type == 1) {
					popupMenu.getMenuInflater().inflate(R.menu.assigned_task,
							popupMenu.getMenu());
				} else {
					popupMenu.getMenuInflater()
							.inflate(R.menu.assigned_task_no_delete,
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
									intent.putExtra("id_task", task.getId());
									context.startActivity(intent);
									break;
								case R.id.action_update_rate:
									if (type == 2) {
										if (update_status_and_rate) {
											Toast.makeText(context,
													"show dialog",
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(
													context,
													context.getResources()
															.getString(
																	R.string.info_update_rate),
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(context,
												"show dialog",
												Toast.LENGTH_SHORT).show();
									}
									break;
								case R.id.action_change_status:
									if (type == 2) {
										if (update_status_and_rate) {
											new DialogChangeStatusTask(context, listStatus, task);
										} else {
											Toast.makeText(
													context,
													context.getResources()
															.getString(
																	R.string.info_update_status),
													Toast.LENGTH_SHORT).show();
										}
									} else {
										new DialogChangeStatusTask(context, listStatus, task);
									}
									break;
								case R.id.action_delete:
									break;

								default:
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
		private TextView project;
		private TextView action;
	}

}
