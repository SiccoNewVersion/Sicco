package com.sicco.task.erp;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.R;
import com.sicco.erp.util.ViewDispatch;
import com.sicco.task.adapter.ReportSteerTaskAdapter;
import com.sicco.task.model.ReportSteerTask;
import com.sicco.task.model.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class DetailTaskActivity extends Activity implements OnClickListener, OnItemClickListener {
	private TextView title, content, assigner, implementers, assigned_at, expired_at, completed_infact, process,
			emptyView, attach_file;
	private ImageView back;
	private SlidingDrawer drawer;
	private ViewDispatch viewDispatch;

	private long id_task;
	private Task task;

	private LinearLayout connectError;
	private ListView listReport;
	private ProgressBar loading;
	private Button retry;

	private ArrayList<ReportSteerTask> arrReportSteers;
	private ReportSteerTaskAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_detail_task);
		
		Intent intent = getIntent();
		id_task = intent.getLongExtra("id_task", -1);
		task = (Task) intent.getSerializableExtra("task");
		
		init();
		long id_cv = task.getId();

		String id = Long.toString(id_cv);
		if (id_cv != -1) {
			getData(id);
		} 

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
		attach_file = (TextView) findViewById(R.id.attach_file);

		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		listReport = (ListView) findViewById(R.id.listReport);
		emptyView = (TextView) findViewById(R.id.empty_view);

		attach_file.setOnClickListener(this);
		back.setOnClickListener(this);

		retry.setOnClickListener(this);
		listReport.setOnItemClickListener(this);

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
		title.setText(task.getTen_cong_viec());
		content.setText(task.getMo_ta());
		assigner.setText(task.getNguoi_giao());
		implementers.setText(task.getNguoi_thuc_hien());
		assigned_at.setText(task.getNgay_bat_dau());
		expired_at.setText(task.getNgay_ket_thuc());
		completed_infact.setText("");
		process.setText(task.getTien_do());

	}

	private String getFilePathFromTaskID(String id_task) {
		// ArrayList<ReportSteerTask> data = getData(getApplicationContext(),
		// getResources().getString(R.string.api_get_steer_report_task),
		// id_task);
		int max = arrReportSteers.size();
		String path = "";
		Log.d("MyDebug", "max : " + max);
		for (int i = 0; i < max; i++) {
			path = arrReportSteers.get(i).getFile();
		}
		return path;
	}

//	private ArrayList<ReportSteerTask> setListReportSteer(String id_task) {
//		arrReportSteers = getData(getApplicationContext(), getResources().getString(R.string.api_get_steer_report_task),
//				id_task
//
		// , new OnLoadListener()
		// {
		// @Override
		// public void onSuccess() {
		// loading.setVisibility(View.GONE);
		// adapter.notifyDataSetChanged();
		//
		// if (adapter.getCount() <= 0) {
		// listReport.setEmptyView(emptyView);
		// }
		//
		// }
		//
		// @Override
		// public void onStart() {
		// loading.setVisibility(View.VISIBLE);
		// connectError.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onFailure() {
		// loading.setVisibility(View.GONE);
		// connectError.setVisibility(View.VISIBLE);
		// }
		// }
//		);
//
//		return arrReportSteers;
//	}

	private ArrayList<ReportSteerTask> getData(String id_task) {
		// this.onLoadListener = onLoadListener;
		// onLoadListener.onStart();
		arrReportSteers = new ArrayList<ReportSteerTask>();

		RequestParams params = new RequestParams();
		params.add("id_cv", id_task);

		AsyncHttpClient client = new AsyncHttpClient();
		client.post(getResources().getString(R.string.api_get_steer_report_task), params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				String jsonRead = response.toString();
				String file = "";
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);

							String id = row.getString("id");
							String handler = row.getString("nguoi_binh_luan");
							String date = row.getString("thoi_gian");
							String content = row.getString("noi_dung");
							file = row.getString("dinh_kem");

							file = file.replace(" ", "%20");

							arrReportSteers.add(new ReportSteerTask(Long.parseLong(id), handler, date, content, file));
						}
						adapter = new ReportSteerTaskAdapter(DetailTaskActivity.this, arrReportSteers);
						listReport.setAdapter(adapter);
						attach_file.setText("abc xyz");
						if (!file.equals("")) {
							attach_file.setText(file);
						} else {
							attach_file.setText(getResources().getString(R.string.no_attach));
						}

						Log.d("MyDebug", "file : " + file);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				// onLoadListener.onSuccess();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
				// onLoadListener.onFailure();
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});
		return arrReportSteers;
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
			String path = getFilePathFromTaskID("" + id_task);
			Log.d("ToanNM", "path : " + path);
			if (!path.equals("")) {
				viewDispatch = new ViewDispatch(getApplicationContext(), path);
			} else

				Toast.makeText(getApplicationContext(),
						getApplicationContext().getResources().getString(R.string.no_attach), Toast.LENGTH_SHORT)
						.show();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.retry:
			getData("" + id_task);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ReportSteerTask reportSteerTask = (ReportSteerTask) arg0.getAdapter().getItem(arg2);
		if (!reportSteerTask.getFile().equals("")) {
			viewDispatch = new ViewDispatch(getApplicationContext(), reportSteerTask.getFile());
		} else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_attach), Toast.LENGTH_SHORT)
					.show();
		}
	}

	private interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFailure();
	}

	private OnLoadListener onLoadListener;
}