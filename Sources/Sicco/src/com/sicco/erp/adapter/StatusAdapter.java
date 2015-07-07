package com.sicco.erp.adapter;

import java.util.ArrayList;

import com.sicco.erp.R;
import com.sicco.erp.model.Status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

public class StatusAdapter extends ArrayAdapter<Status> {
	Context context;
	int Resource;
	ArrayList<Status> listStatus;
	
	public StatusAdapter(Context context, int resource,
			ArrayList<Status> listStatus) {
		super(context, resource, listStatus);
		this.context = context;
		this.Resource = resource;
		this.listStatus = listStatus;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
		.inflate(Resource, parent,false);
		
		CheckedTextView item = (CheckedTextView)view.findViewById(R.id.ckStatus);
		Status status = getItem(position);
		item.setText(status.getStatus());
		
		return view;
	}
	
}
