package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.model.Dispatch;

public class DispatchAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Dispatch> data;

	public DispatchAdapter(Context context) {
		this.context = context;
		Dispatch dispatch = new Dispatch(context);
		this.data = dispatch.getData();
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
		ViewHolder holder;
		Dispatch dispatch = getItem(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_dispatch,
					viewGroup, false);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.title);
			holder.description = (TextView) view.findViewById(R.id.description);
			holder.approval = (TextView) view.findViewById(R.id.approval);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.title.setText(dispatch.getTitle());
		holder.description.setText(dispatch.getDescription());
		holder.approval.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
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
