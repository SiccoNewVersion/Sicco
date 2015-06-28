package com.sicco.erp.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.adapter.ExpandableListUserAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;

public class DialogChoseUser {

	private Context context;
	private ArrayList<Department> listDep;
	private ArrayList<User> allUser;
	private HashMap<String, ArrayList<User>> listUser;
	private ArrayList<User> listChecked = new ArrayList<User>();
	private ExpandableListUserAdapter adapter;
	private int mCurrentExpandedGroup = -1;

	public DialogChoseUser(Context context, ArrayList<Department> listDep,
			ArrayList<User> allUser, ArrayList<User> listChecked) {
		this.context = context;
		this.listDep = listDep;
		this.allUser = allUser;
		this.listChecked = listChecked;
		
		listUser = getData(listDep, allUser);
		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dialog_chose_user, null);
		layout.setMinimumWidth((int) (rect.width() * 0.99f));
		layout.setMinimumHeight((int) (rect.height() * 0.99f));
	
		final ExpandableListView listView = (ExpandableListView) layout.findViewById(R.id.listUser);
		adapter = new ExpandableListUserAdapter(context, listDep, listUser, listChecked);
		listView.setAdapter(adapter);
		
		//click child
		listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long arg4) {
				
				return true;
			}
		});
		
		//click group
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
					long arg3) {
				int count = adapter.getGroupCount();
				for(int i = 0; i < count; i++){
					if(i != arg2){
						listView.collapseGroup(i);
					}
				}
				
				if(mCurrentExpandedGroup >= 0 && mCurrentExpandedGroup < count){
					if(mCurrentExpandedGroup == arg2){
						listView.collapseGroup(arg2);
						mCurrentExpandedGroup = -1;
					} else {
						listView.expandGroup(arg2, false);
					}
				} else {
					listView.expandGroup(arg2, false);
				}
				return true;
			}
		});
		
		//Expand
		listView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int arg0) {
				mCurrentExpandedGroup = arg0;
				listView.setSelectedGroup(arg0);
				
			}
		});
		
		//Collapse
		listView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				View view = adapter.getGroupView(groupPosition, true, null, null);
				view.invalidate();
				
			}
		});
		
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
		
		Button btnDone = (Button) layout.findViewById(R.id.done);
		btnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Log.d("LuanDT", "listChecked.size(): " + listChecked.size());
				Toast.makeText(context, "listChecked.size(): " + listChecked.size(), Toast.LENGTH_SHORT).show();
				alertDialog.dismiss();
			}
		});
	}
	
	public HashMap<String, ArrayList<User>> getData(
			ArrayList<Department> listDep, final ArrayList<User> users) {
		listUser = new HashMap<String, ArrayList<User>>();
		for (int i = 0; i < listDep.size(); i++) {
			Department department = listDep.get(i);
			ArrayList<User> data = new ArrayList<User>();
			for (int j = 0; j < users.size(); j++) {
				User temp = users.get(j);
				if (temp.getDepartment().equals(department.getId())) {
					data.add(new User(temp.getId(), temp.getUsername(), temp
							.getDepartment()));
				}
			}
			listUser.put(listDep.get(i).getDepartmentName(), data);
		}
		return listUser;
	}
}