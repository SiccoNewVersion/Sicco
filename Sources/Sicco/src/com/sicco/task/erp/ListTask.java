package com.sicco.task.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.AdapterView.OnItemClickListener;

import com.sicco.erp.R;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.TaskAdapter;
import com.sicco.task.model.Task;

public class ListTask extends Activity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	private TextView emptyView;
	private ListView listTask;
	private ProgressBar loading;
	private Button retry;
	private Task task;
	private ViewDispatch viewDispatch;
	private ArrayList<Task> arrTask;
	private TaskAdapter adapter;

	private TextView title_actionbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_assigned_task);

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
		arrTask.add(new Task(1, "ten_cong_viec", "nguoi_thuc_hien", "du_an",
				"tien_do", "ngay_bat_dau", "ngay_ket_thuc", "nguoi_giao",
				"dinh_kem", "phong_ban", "mo_ta", "code", "id_du_an",
				"id_phong_ban"));
//		arrTask = task.getData(ListTask.this,
//				getResources().getString(R.string.api_get_task),
//				new Task.OnLoadListener() {
//
//					@Override
//					public void onStart() {
//						loading.setVisibility(View.VISIBLE);
//						connectError.setVisibility(View.GONE);
//					}
//
//					@Override
//					public void onSuccess() {
//						loading.setVisibility(View.GONE);
//						adapter.notifyDataSetChanged();
//						if (adapter.getCount() <= 0) {
//							listTask.setEmptyView(emptyView);
//						}
//					}
//
//					@Override
//					public void onFalse() {
//						loading.setVisibility(View.GONE);
//						connectError.setVisibility(View.VISIBLE);
//					}
//				});
		adapter = new TaskAdapter(ListTask.this, arrTask, "ListTask");
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
		viewDispatch = new ViewDispatch(ListTask.this, task.getDinh_kem());
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