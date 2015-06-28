package com.sicco.erp.adapter;

import java.util.ArrayList;

import com.sicco.erp.R;
import com.sicco.erp.model.ReportSteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportSteerAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ReportSteer> data;
//	private int intResource;
	public ReportSteerAdapter(Context context, ArrayList<ReportSteer> data) {
		this.context = context;
		this.data = data;
//		this.intResource = resource;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public ArrayList<ReportSteer> getData() {
		return data;
	}

	public void setData(ArrayList<ReportSteer> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	

	@Override
	public ReportSteer getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return data.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ReportSteer reportSteer = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_report_steer, parent,false);
			viewHolder = new ViewHolder();
			viewHolder.handler = (TextView)convertView.findViewById(R.id.txtHandler);
			viewHolder.date = (TextView)convertView.findViewById(R.id.txtDate);
			viewHolder.contentReport = (TextView)convertView.findViewById(R.id.txtContentReportSteer);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.handler.setText(reportSteer.getHandler());
		viewHolder.date.setText(reportSteer.getDate());
		viewHolder.contentReport.setText(reportSteer.getContent());
		
		return convertView;
	}
	
	private class ViewHolder{
		TextView handler,date,contentReport;
	}
}
