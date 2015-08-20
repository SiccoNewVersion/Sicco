package com.sicco.task.erp;

import com.sicco.erp.R;
import com.sicco.erp.util.ViewDispatch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class DetailTaskActivity extends Activity implements OnClickListener {
	private TextView title, content, assigner, implementers, assigned_at, expired_at, completed_infact, process;
	private Button attach_file;
	private ImageView back;
	private SlidingDrawer drawer;

	private ViewDispatch viewDispatch;

	private long id_task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_detail_task);

		Intent intent = getIntent();
		id_task = intent.getLongExtra("id_task", -1);

		init();
	}

	private void init() {

		drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
		back = (ImageView) findViewById(R.id.back);

		title = (TextView) findViewById(R.id.title);
		content = (TextView) findViewById(R.id.task_content);
		assigner = (TextView) findViewById(R.id.assigner);
		implementers = (TextView) findViewById(R.id.implementers);
		assigned_at = (TextView) findViewById(R.id.assigned_at);
		expired_at = (TextView) findViewById(R.id.expired_at);
		completed_infact = (TextView) findViewById(R.id.completed_infact);
		process = (TextView) findViewById(R.id.process);
		attach_file = (Button) findViewById(R.id.attach_file);
		attach_file.setOnClickListener(this);
		back.setOnClickListener(this);
		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {

			}
		});
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {

			}
		});

		// setData
		title.setText("");
		content.setText("");
		assigner.setText("");
		implementers.setText("");
		assigned_at.setText("");
		expired_at.setText("");
		completed_infact.setText("");
		process.setText("");

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		switch (id) {
		case R.id.attach_file:
			// open ViewDispatch
			viewDispatch = new ViewDispatch(DetailTaskActivity.this, "");
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}