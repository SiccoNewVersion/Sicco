package com.sicco.erp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.sicco.erp.adapter.TouchImageAdapter;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.view.ExtendedViewPager;

public class ViewDispatchActivity extends Activity implements OnClickListener {
	private ImageView back;
	private TextView title;
	private ExtendedViewPager viewPager;
	private Dispatch dispatch;
	private ArrayList<String> arrayImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_dispatch);
		Bundle bundle = getIntent().getBundleExtra("bundle");
		dispatch = (Dispatch) bundle.getSerializable("dispatch");
		if(dispatch==null){
			finish();
		}
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		viewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
		//
		back.setOnClickListener(this);
		title.setText(dispatch.getTitle());
		arrayImage = dispatch.getImage_url();
		TouchImageAdapter adapter = new TouchImageAdapter(ViewDispatchActivity.this, arrayImage);
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		}
	}

}
