package com.sicco.erp.adapter;

import java.util.ArrayList;

import com.sicco.erp.R;
import com.sicco.erp.SteerReportActivity;
import com.sicco.erp.model.Dispatch;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Dispatch> data;

	public TaskAdapter(Context context, ArrayList<Dispatch> data) {
		this.context = context;
		this.data = data;
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
			holder.approval.setText(context.getResources().getString(R.string.task));
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText(dispatch.getTitle());
		holder.description.setText(dispatch.getDescription());
		holder.approval.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PopupMenu popupMenu = new PopupMenu(context, holder.approval);
				popupMenu.getMenuInflater().inflate(R.menu.menu_task, popupMenu.getMenu());
				popupMenu.show();
				popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Intent intent = new Intent();
						switch (item.getItemId()) {
						case R.id.action_handle:
							Toast.makeText(context, item.getTitle().toString(),Toast.LENGTH_SHORT).show();
							break;
						case R.id.action_steer:
							intent.setClass(context, SteerReportActivity.class);
							intent.putExtra("id", "" + dispatch.getId());
							context.startActivity(intent);
							break;
						case R.id.action_change_status:
							Toast.makeText(context, item.getTitle().toString(),Toast.LENGTH_SHORT).show();
							break;
						case R.id.action_job_transfer:
							Toast.makeText(context, item.getTitle().toString(),Toast.LENGTH_SHORT).show();
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
	}
}