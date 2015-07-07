package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.sicco.erp.ChangeStatusDispatch;
import com.sicco.erp.ConvertDispatchActivity;
import com.sicco.erp.HomeActivity;
import com.sicco.erp.R;
import com.sicco.erp.SteerReportActivity;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Status;
import com.sicco.erp.model.User;
import com.sicco.erp.util.DialogChangeStatusDispatch;
import com.sicco.erp.util.DialogChoseUser;

public class TaskAdapter extends BaseAdapter {
	private Context context;
	ArrayList<Status> listStatus;
	private ArrayList<Dispatch> data;
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private ArrayList<User> listChecked;
	public static String flag = "";
	
	private Cursor cursor;
	private NotificationDBController db;
	int type;


	public TaskAdapter(Context context, ArrayList<Dispatch> data, int type) {
		this.context = context;
		this.data = data;
		this.type = type;

		listChecked = new ArrayList<User>();

		listDep = HomeActivity.listDep;
		allUser = HomeActivity.allUser;
	}

	public void setData(ArrayList<Dispatch> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Dispatch getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return data.get(arg0).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		final ViewHolder holder;
		final Dispatch dispatch = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_dispatch,
					viewGroup, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.description = (TextView) view.findViewById(R.id.description);
			holder.approval = (TextView) view.findViewById(R.id.approval);
			holder.approval.setText(context.getResources().getString(
					R.string.task));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		if (type == 1) {
			long id = dispatch.getId();
			String state = querryFromDB(context, id);
			if (state
					.equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)) {
				view.setBackgroundColor(Color.CYAN);
			} else {
				view.setBackgroundColor(Color.WHITE);
			}
		}
		
		holder.title.setText(dispatch.getNumberDispatch());
		holder.description.setText(dispatch.getDescription());
		holder.approval.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PopupMenu popupMenu = new PopupMenu(context, holder.approval);
				popupMenu.getMenuInflater().inflate(R.menu.menu_task,
						popupMenu.getMenu());
				popupMenu.show();
				popupMenu
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								Intent intent = new Intent();
								switch (item.getItemId()) {
								case R.id.action_handle:
									flag = "handle";
									new DialogChoseUser(context, dispatch,
											listDep, allUser, listChecked);
									break;
								case R.id.action_steer:
									intent.setClass(context,
											SteerReportActivity.class);
									intent.putExtra("dispatch", dispatch);
									context.startActivity(intent);
									break;
								case R.id.action_change_status:
									listStatus = new ArrayList<Status>();
									
									listStatus.add(new Status(context.getResources().getString(R.string.need_approval),Long.parseLong("1")));
									listStatus.add(new Status(context.getResources().getString(R.string.need_handle),Long.parseLong("2")));
									listStatus.add(new Status(context.getResources().getString(R.string.pause_handle),Long.parseLong("3")));
									listStatus.add(new Status(context.getResources().getString(R.string.finish_handle),Long.parseLong("4")));
									new DialogChangeStatusDispatch(context, listStatus,dispatch);
									break;
								case R.id.action_job_transfer:
									intent.setClass(context,
											ConvertDispatchActivity.class);
									intent.putExtra("dispatch", dispatch);
									context.startActivity(intent);
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
		TextView title;
		TextView description;
		TextView approval;
		LinearLayout mainLayout;
	}
	
	String querryFromDB(Context context, long position) {
		String state = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DISPATCH_COL + " = " + position;
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				// int did = cursor
				// .getInt(cursor
				// .getColumnIndexOrThrow(NotificationDBController.DISPATCH_COL));
				state = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.DSTATE_COL));
			} while (cursor.moveToNext());
		}
		return state;
	}
}