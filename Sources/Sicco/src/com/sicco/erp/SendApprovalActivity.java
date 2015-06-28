package com.sicco.erp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SendApprovalActivity extends Activity implements OnClickListener {
	private ImageView back, send;
	private EditText document;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_send_approval);
		init();
	}

	private void init() {
		back = (ImageView) findViewById(R.id.back);
		send = (ImageView) findViewById(R.id.send);
		document = (EditText) findViewById(R.id.document);
		// click
		back.setOnClickListener(this);
		send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.back:
			finish();
			break;
		case R.id.send:
			Toast.makeText(SendApprovalActivity.this, "SEND", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}