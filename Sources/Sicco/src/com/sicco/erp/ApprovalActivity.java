package com.sicco.erp;

import java.util.ArrayList;

import com.sicco.erp.adapter.DispatchAdapter;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.util.Keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ApprovalActivity extends Activity implements OnClickListener {
	private LinearLayout searchView;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	private ListView listDispatch;
	private DispatchAdapter dispatchAdapter;
	private ArrayList<Dispatch> arrDispatch;
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
		listDispatch = (ListView) findViewById(R.id.listDispatch);
		//click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		//set adapter
		dispatchAdapter = new DispatchAdapter(ApprovalActivity.this);
		listDispatch.setAdapter(dispatchAdapter);
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
		}
	}
	private void showSearchView(){
		searchView.setVisibility(View.VISIBLE);
		searchView.requestFocus();
		Keyboard.showKeyboard(ApprovalActivity.this, editSearch);
		editSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				if(arg0.toString().trim().length()>0){
					empty.setVisibility(View.VISIBLE);
				}else{
					empty.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}
	private void closeSearchView(){
		Keyboard.hideKeyboard(ApprovalActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}
}
