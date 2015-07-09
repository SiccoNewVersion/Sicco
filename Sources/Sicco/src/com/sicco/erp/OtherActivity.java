package com.sicco.erp;

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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.adapter.TaskAdapter;
import com.sicco.erp.database.NotificationDBController;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.service.GetAllNotificationService;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.Utils;
import com.sicco.erp.util.ViewDispatch;

public class OtherActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	public static LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	public static TextView emptyView;
	public static ListView listDispatch;
	public static ProgressBar loading;
	private Button retry;
	public static TaskAdapter adapter;
	public static ArrayList<Dispatch> arrDispatch;
	private Dispatch dispatch;
	private TextView title_actionbar;
	private ViewDispatch viewDispatch;

	public static boolean otherActivitySelected = false;

	NotificationDBController db;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_approval);

		otherActivitySelected = true;

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
		listDispatch = (ListView) findViewById(R.id.listDispatch);
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		title_actionbar = (TextView) findViewById(R.id.title_actionbar);
		title_actionbar.setText(getResources().getString(R.string.cv_cac_loai));
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listDispatch.setOnItemClickListener(this);
		// set adapter
		dispatch = new Dispatch(OtherActivity.this);
		arrDispatch = dispatch.getData(OtherActivity.this, getResources()
				.getString(R.string.api_get_dispatch_other),
				new OnLoadListener() {

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
							listDispatch.setEmptyView(emptyView);
						}
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				}, 1);

		db = NotificationDBController.getInstance(getApplicationContext());
		adapter = new TaskAdapter(OtherActivity.this, arrDispatch, 1);
		listDispatch.setAdapter(adapter);
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
			adapter.setData(dispatch.getData(OtherActivity.this, getResources()
					.getString(R.string.api_get_dispatch_other),
					new OnLoadListener() {

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
								listDispatch.setEmptyView(emptyView);
							}
						}

						@Override
						public void onFalse() {
							loading.setVisibility(View.GONE);
							connectError.setVisibility(View.VISIBLE);
						}
					}, 1));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Dispatch dispatch = (Dispatch) arg0.getAdapter().getItem(arg2);
		viewDispatch = new ViewDispatch(OtherActivity.this,
				dispatch.getContent());

		db.checkedDisPatch(dispatch, dispatch.getId());
		
		String state = querryFromDB(getApplicationContext(), arg2);
		if(state.equalsIgnoreCase(NotificationDBController.NOTIFICATION_STATE_NEW)){
			int count = querryFromDB(getApplicationContext());
			setCount(count);
		}
		startGetAllNotificationService();
		
		adapter.notifyDataSetChanged();
	}

	void setCount(int count) {
		if (count != 0) {
			count--;
		} else if (count == 0) {
			cancelNotification(getApplicationContext(), 3);
		}
		Utils.saveInt(getApplicationContext(),
				GetAllNotificationService.CL_KEY, count);
	}
	
	String querryFromDB(Context context, long position) {
		String state = "";
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME + " where "
				+ NotificationDBController.DISPATCH_COL + " = " + position;
//				+ " order by " + NotificationDBController.DSTATE_COL + " DESC";
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

	int querryFromDB(Context context) {
		int count = 0;
		db = NotificationDBController.getInstance(context);
		cursor = db.query(NotificationDBController.DISPATCH_TABLE_NAME, null,
				null, null, null, null, null);
		String sql = "Select * from "
				+ NotificationDBController.DISPATCH_TABLE_NAME
				+ " where "
				+ NotificationDBController.DSTATE_COL
				+ " = \"new\""
				+ " and "
				+ NotificationDBController.D_TYPE_COL
				+ " = " + 1;
		Log.d("ToanNM", "otheractivity sql : " + sql);
		cursor = db.rawQuery(sql, null);
		count = cursor.getCount();
		Toast.makeText(getApplicationContext(), "OtherActivity : " + count , Toast.LENGTH_SHORT).show();
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
			searchView.setVisibility(View.GONE);
			editSearch.setText("");
		} else {
			super.onBackPressed();
		}
	}

	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		Keyboard.showKeyboard(OtherActivity.this, editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.toString().trim().length() > 0) {
					empty.setVisibility(View.VISIBLE);
				} else {
					empty.setVisibility(View.GONE);
				}
				ArrayList<Dispatch> searchData = dispatch.search(arg0
						.toString().trim());
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
		Keyboard.hideKeyboard(OtherActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}

	// ToanNM
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
	// // End of ToanNM
}