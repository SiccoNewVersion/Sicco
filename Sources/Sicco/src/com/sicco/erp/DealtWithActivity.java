package com.sicco.erp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class DealtWithActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);
	}
}