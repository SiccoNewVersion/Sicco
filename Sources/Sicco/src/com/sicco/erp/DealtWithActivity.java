package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
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

import com.sicco.erp.adapter.TaskAdapter;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.util.Keyboard;
import com.sicco.erp.util.ViewDispatch;

public class DealtWithActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	private LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	private TextView emptyView;
	private ListView listDispatch;
	private ProgressBar loading;
	private Button retry;
	private TaskAdapter adapter;
	private ArrayList<Dispatch> arrDispatch;
	private Dispatch dispatch;
	private TextView title_actionbar;
	private ViewDispatch viewDispatch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_approval);
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
		title_actionbar.setText(getResources().getString(R.string.cv_xu_ly));
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listDispatch.setOnItemClickListener(this);
		// set adapter
		dispatch = new Dispatch(DealtWithActivity.this);
		arrDispatch = dispatch.getData(
				getResources().getString(R.string.api_get_dispatch_handle),
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
				});
		adapter = new TaskAdapter(DealtWithActivity.this, arrDispatch, 0);
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
			adapter.setData(dispatch.getData(
					getResources().getString(R.string.api_get_dispatch_handle),
					new OnLoadListener() {

						@Override
						public void onStart() {
							loading.setVisibility(View.VISIBLE);
							connectError.setVisibility(View.GONE);
						}

						@Override
						public void onSuccess() {
							loading.setVisibility(View.GONE);
							// listDispatch.setAdapter(dispatchAdapter);
							adapter.notifyDataSetChanged();
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
		Dispatch dispatch = (Dispatch) arg0.getAdapter().getItem(arg2);
		viewDispatch = new ViewDispatch(DealtWithActivity.this, dispatch.getContent());
		// Intent intent = new Intent(DealtWithActivity.this,
		// ViewDispatchActivity.class);
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("dispatch", dispatch);
		// intent.putExtra("bundle", bundle);
		// startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (searchView.getVisibility() == View.VISIBLE) {
			searchView.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	private void showSearchView() {
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		Keyboard.showKeyboard(DealtWithActivity.this, editSearch);
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
		Keyboard.hideKeyboard(DealtWithActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}
	
	@Override
	protected void onStart() {
		Log.d("NgaDV", "onStart");
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.d("NgaDV", "onResume");
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d("NgaDV", "onPause");
		super.onPause();
	}
	@Override
	protected void onStop() {
		Log.d("NgaDV", "onStop");
		super.onStop();
	}
	
}