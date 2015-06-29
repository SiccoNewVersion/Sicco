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
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;
import com.sicco.erp.SendApprovalActivity;
import com.sicco.erp.adapter.ExpandableListUserAdapter;
import com.sicco.erp.adapter.TaskAdapter;
import com.sicco.erp.model.Department;
import com.sicco.erp.model.User;

public class DialogShowHandler {

	private Context context;
	private ArrayList<User> listChecked;
	private String handler;

	public DialogShowHandler(Context context, ArrayList<User> listChecked) {
		this.context = context;
		this.listChecked = listChecked;

		showDialog();
	}

	private void showDialog() {
		Rect rect = new Rect();
		Window window = ((Activity) context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rect);

		LayoutInflater inflater = LayoutInflater.from(context);
		View layout = inflater.inflate(R.layout.dialog_show_handler, null);
		layout.setMinimumWidth((int) (rect.width() * 1f));
//		layout.setMinimumHeight((int) (rect.height() * 1f));

		TextView title = (TextView) layout.findViewById(R.id.title_actionbar);
		TextView txtListHandeler = (TextView) layout.findViewById(R.id.listHandler);
		
		handler = context.getResources()
				.getString(R.string.handler);
		for (int i = 0; i < listChecked.size(); i++) {
			handler += listChecked.get(i).getUsername() + "; ";
		}
		txtListHandeler.setText(handler);
		
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

			}
		});
	}

}
