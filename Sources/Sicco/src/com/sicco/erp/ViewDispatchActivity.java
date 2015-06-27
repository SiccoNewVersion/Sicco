package com.sicco.erp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sicco.erp.adapter.TouchImageAdapter;
import com.sicco.erp.view.ExtendedViewPager;
import com.sicco.erp.view.TouchImageView;

public class ViewDispatchActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_dispatch);
		ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(new TouchImageAdapter(ViewDispatchActivity.this));
	}

}
