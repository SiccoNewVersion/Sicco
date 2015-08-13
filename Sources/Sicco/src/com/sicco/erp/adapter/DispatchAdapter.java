package com.sicco.erp.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.SendApprovalActivity;
import com.sicco.erp.model.Dispatch;

public class DispatchAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Dispatch> data;

	public DispatchAdapter(Context context, ArrayList<Dispatch> data) {
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
		ViewHolder holder;
		final Dispatch dispatch = getItem(position);
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
		
//		if(dispatch.getPheduyet().equals("1")){
//			holder.approval.setVisibility(View.GONE);
//		} else {
//			holder.approval.setVisibility(View.VISIBLE);
//		}
//		
//		if(dispatch.getStatus().equals("1")){
//			holder.title.setTextColor(context.getResources().getColor(R.color.black));
//			holder.description.setTextColor(context.getResources().getColor(R.color.black));
//		} else if(dispatch.getStatus().equals("2")){
//			holder.title.setTextColor(context.getResources().getColor(R.color.red));
//			holder.description.setTextColor(context.getResources().getColor(R.color.red));
//		} else if(dispatch.getStatus().equals("3")){
//			holder.title.setTextColor(context.getResources().getColor(R.color.yellow));
//			holder.description.setTextColor(context.getResources().getColor(R.color.yellow));
//		} else if(dispatch.getStatus().equals("4")){
//			holder.title.setTextColor(context.getResources().getColor(R.color.gray));
//			holder.description.setTextColor(context.getResources().getColor(R.color.gray));
//		} else if(dispatch.getStatus().equals("5")){
//			holder.title.setTextColor(context.getResources().getColor(R.color.green));
//			holder.description.setTextColor(context.getResources().getColor(R.color.green));
//		}
		
		holder.title.setText(dispatch.getNumberDispatch());
		holder.description.setText(dispatch.getDescription());
		holder.approval.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, SendApprovalActivity.class);
				intent.putExtra("dispatch", dispatch);
				context.startActivity(intent);
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
