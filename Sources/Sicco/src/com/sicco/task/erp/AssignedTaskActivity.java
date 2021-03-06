package com.sicco.task.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.sicco.erp.R;
import com.sicco.erp.adapter.SpinnerStatusAdapter;
import com.sicco.erp.model.Status;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.TaskAdapter;
import com.sicco.task.model.Task;

public class AssignedTaskActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	public static Context context;
	public static LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	public static TextView emptyView;
	public static ListView listTask;
	public static ProgressBar loading;
	private Button retry;
	public static Task task;
	private ViewDispatch viewDispatch;
	public static ArrayList<Task> arrTask;
	public static TaskAdapter adapter;

	private TextView title_actionbar;
	public static boolean AssignedTaskActivity = false;

	private Button btnAssignNewTask;
	private Spinner spnFilter;
	private SpinnerStatusAdapter spinnerStatusAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assigned_task);

		AssignedTaskActivity = true;
		ListTask.ListTaskActivity = false;

		init();

	}

	private void init() {
		context = AssignedTaskActivity.this;
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
		btnAssignNewTask = (Button) findViewById(R.id.btnAssignNew);
		title_actionbar
				.setText(getResources().getString(R.string.viec_da_giao));
		title_actionbar.setVisibility(View.GONE);
		spnFilter = (Spinner) findViewById(R.id.spnFilter);
		spnFilter.setVisibility(View.VISIBLE);
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listTask.setOnItemClickListener(this);
		btnAssignNewTask.setOnClickListener(this);

		ArrayList<Status> listStatus = new ArrayList<Status>();
		listStatus.add(new Status(getResources().getString(R.string.all),
				Task.FILTER_ALL_TYPE));
		listStatus.add(new Status(
				getResources().getString(R.string.chua_xu_ly),
				Task.FILTER_CXL_TYPE));
		listStatus.add(new Status(getResources().getString(
				R.string.dang_xu_ly_trong_han), Task.FILTER_DXLTH_TYPE));
		listStatus.add(new Status(getResources().getString(
				R.string.dang_xu_ly_qua_han), Task.FILTER_DXLQH_TYPE));

		spinnerStatusAdapter = new SpinnerStatusAdapter(
				getApplicationContext(), listStatus);
		spnFilter.setAdapter(spinnerStatusAdapter);

		spnFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Status status = (Status) parent.getAdapter().getItem(position);
				if (!arrTask.isEmpty()) {
					// Log.d("TuNT", "data size: "+task.filter(arrTask,
					// status.getKey()).size());
					adapter.setData(task.filter(arrTask, status.getKey()));
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	// display lisview
	public static void displayLisview() {
		// set adapter
		task = new Task(context);
		arrTask = new ArrayList<Task>();
		arrTask = task.getData(context,
				context.getString(R.string.api_get_assigned_task),
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
		adapter = new TaskAdapter(context, arrTask, 1);
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
		case R.id.btnAssignNew:
			Intent intent = new Intent(AssignedTaskActivity.this,
					AssignTaskActivity.class);
			startActivity(intent);
			break;
		case R.id.retry:
			adapter.setData(task.getData(AssignedTaskActivity.this,
					getResources().getString(R.string.api_get_assigned_task),
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
		Intent intent = new Intent(this, DetailTaskActivity.class);
		intent.putExtra("task", task);
		startActivity(intent);
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
		Keyboard.showKeyboard(AssignedTaskActivity.this, editSearch);
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
		Keyboard.hideKeyboard(AssignedTaskActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}

	@Override
	protected void onResume() {
		spnFilter.setSelection(0);
		displayLisview();
		super.onResume();
	}

}