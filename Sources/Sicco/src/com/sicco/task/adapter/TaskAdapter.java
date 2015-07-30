package com.sicco.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.SteerReportActivity;
import com.sicco.task.erp.SteerReportTaskActivity;
import com.sicco.task.model.Task;

public class TaskAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Task> data;
	private String type;

	public TaskAdapter(Context context, ArrayList<Task> data, String type) {
		super();
		this.context = context;
		this.data = data;
		this.type = type;
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
			holder.handler = (TextView) view.findViewById(R.id.handle);
			holder.project = (TextView) view.findViewById(R.id.project);
			holder.action = (TextView) view.findViewById(R.id.action);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.taskName.setText(task.getTen_cong_viec());
		holder.handler.setText(task.getNguoi_thuc_hien());
		holder.project.setText(task.getDu_an());

		holder.action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PopupMenu popupMenu = new PopupMenu(context, holder.action);
				if (type.equals("AssignedTaskActivity")) {
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
										break;
									case R.id.action_change_status:
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
