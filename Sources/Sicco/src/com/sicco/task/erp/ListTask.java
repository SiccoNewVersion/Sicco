package com.sicco.task.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sicco.erp.R;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.Utils;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.TaskAdapter;
import com.sicco.task.model.Task;

public class ListTask extends Activity implements OnClickListener,
		OnItemClickListener {

	public static LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	public static TextView emptyView;
	public static ListView listTask;
	public static ProgressBar loading;
	private Button retry;
	private Task task;
	private ViewDispatch viewDispatch;
	public static ArrayList<Task> arrTask;
	public static TaskAdapter adapter;

	private TextView title_actionbar;
	public static boolean ListTaskActivity = false;
	
	NotificationDBController db;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assigned_task);

		ListTaskActivity = true;
		AssignedTaskActivity.AssignedTaskActivity = false;
		
		init();

	}

	private void init() {
		searchView = (LinearLayout) findViewById(R.id.searchview);
		back = (ImageView) findViewById(R.id.back);
		search = (ImageView) findViewById(R.id.search);
		close = (ImageView) searchView.findViewById(R.id.close);
		empty = (ImageView) searchView.findViewById(R.id.empty);
		editSearch = (EditText) searchView.findViewById(R.id.edit_search);
		emptyView = (TextView) findViewById(R.id.empty_view);
		listTask = (ListView) findViewById(R.id.listTask);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		title_actionbar = (TextView) findViewById(R.id.title_actionbar);
		title_actionbar.setText(getResources().getString(
				R.string.viec_duoc_giao));
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listTask.setOnItemClickListener(this);

		// set adapter
		task = new Task(ListTask.this);
		arrTask = new ArrayList<Task>();
		arrTask = task.getData(ListTask.this,
				getResources().getString(R.string.api_get_task),
				new Task.OnLoadListener() {

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						adapter.notifyDataSetChanged();
						if (adapter.getCount() <= 0) {
							listTask.setEmptyView(emptyView);
						}
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});
		adapter = new TaskAdapter(ListTask.this, arrTask, 2);
		listTask.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.search:
			showSearchView();
			break;
		case R.id.close:
			closeSearchView();
			break;
		case R.id.empty:
			editSearch.setText("");
			break;
		case R.id.retry:
			adapter.setData(task.getData(ListTask.this, getResources()
					.getString(R.string.api_get_task),
					new Task.OnLoadListener() {

						@Override
						public void onStart() {
							loading.setVisibility(View.VISIBLE);
							connectError.setVisibility(View.GONE);
						}

						@Override
						public void onSuccess() {
							loading.setVisibility(View.GONE);
							adapter.notifyDataSetChanged();
							if (adapter.getCount() <= 0) {
								listTask.setEmptyView(emptyView);
							}
						}

						@Override
						public void onFalse() {
							loading.setVisibility(View.GONE);
							connectError.setVisibility(View.VISIBLE);
						}
					}));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Task task = (Task) arg0.getAdapter().getItem(arg2);
		if (!task.getDinh_kem().equals("")) {
			viewDispatch = new ViewDispatch(ListTask.this,
					task.getDinh_kem());
		} else {
			Toast.makeText(ListTask.this, getResources().getString(R.string.no_attach), Toast.LENGTH_SHORT).show();
		}
		
		String state = querryFromDB(getApplicationContext(), arg2);
		if(state.equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)){
			int count = querryFromDB(getApplicationContext());
			setCount(count);
		}
		startGetAllNotificationService();
		db.checkedTask(arrTask.get(arg2), arrTask.get(arg2).getId());
		adapter = new TaskAdapter(ListTask.this, arrTask, 2);
		listTask.setAdapter(adapter);
		listTask.setSelection(arg2);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		 startGetAllNotificationService();
	}

	//
	void startGetAllNotificationService() {
		Intent intent = new Intent(getApplicationContext(),
				GetAllNotificationService.class);
		intent.putExtra("ACTION", 0);
		getApplicationContext().startService(intent);
	}
	
	void setCount(int count) {
		if (count != 0) {
			count--;
		} else if (count == 0) {
			cancelNotification(getApplicationContext(), 4);
		}
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CV_KEY, count);
		Log.d("MyDebug", "CV_Key : " + count);
	}
	
	String querryFromDB(Context context, long position) {
		String state = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.TASK_TABLE_NAME + " where "
				+ NotificationDBController.ID_COL + " = " + position;
//				+ " order by " + NotificationDBController.DSTATE_COL + " DESC";
		cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			do {
				// int did = cursor
				// .getInt(cursor
				// .getColumnIndexOrThrow(NotificationDBController.DISPATCH_COL));
				state = cursor
						.getString(cursor
								.getColumnIndexOrThrow(NotificationDBController.TRANGTHAI_COL));
			} while (cursor.moveToNext());
		}
		return state;
	}

	int querryFromDB(Context context) {
		int count = 0;
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.TASK_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.TASK_TABLE_NAME
				+ " where "
				+ NotificationDBController.TRANGTHAI_COL
				+ " = \"new\"";
		cursor = db.rawQuery(sql, null);
		count = cursor.getCount();
		Toast.makeText(getApplicationContext(), "ListTask : " + count , Toast.LENGTH_SHORT).show();
		return count;
	}

	void cancelNotification(Context context, int notification_id) {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(notificationServiceStr);
		mNotificationManager.cancel(notification_id);
	}

	@Override
	public void onBackPressed() {
		if (searchView.getVisibility() == View.VISIBLE) {
			editSearch.setText("");
			searchView.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		Keyboard.showKeyboard(ListTask.this, editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.toString().trim().length() > 0) {
					empty.setVisibility(View.VISIBLE);
				} else {
					empty.setVisibility(View.GONE);
				}
				ArrayList<Task> searchData = task.search(
						arg0.toString().trim(), arrTask);
				adapter.setData(searchData);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	private void closeSearchView() {
		Keyboard.hideKeyboard(ListTask.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}

}
