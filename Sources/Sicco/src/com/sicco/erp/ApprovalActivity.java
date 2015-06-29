package com.sicco.erp;

import java.io.File;
import java.util.ArrayList;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.Toast;

import com.sicco.erp.adapter.DispatchAdapter;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.model.Dispatch.OnLoadListener;
import com.sicco.erp.util.DownloadFile;
import com.sicco.erp.util.DownloadFile.OnDownloadListener;
import com.sicco.erp.util.Keyboard;

public class ApprovalActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private LinearLayout searchView, connectError;
	private ImageView back, search, close, empty;
	private EditText editSearch;
	private ListView listDispatch;
	private ProgressBar loading;
	private Button retry;
	private DispatchAdapter dispatchAdapter;
	private ArrayList<Dispatch> arrDispatch;
	private Dispatch dispatch;
	private TextView title_actionbar;
	private AlertDialog aDialog;
	private ProgressDialog dialog;
	private DownloadFile downloadFile;

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
		loading = (ProgressBar) findViewById(R.id.loading);
		retry = (Button) findViewById(R.id.retry);
		connectError = (LinearLayout) findViewById(R.id.connect_error);
		title_actionbar = (TextView) findViewById(R.id.title_actionbar);
		title_actionbar.setText(getResources().getString(R.string.cv_can_phe));
		// click
		back.setOnClickListener(this);
		search.setOnClickListener(this);
		close.setOnClickListener(this);
		empty.setOnClickListener(this);
		retry.setOnClickListener(this);
		listDispatch.setOnItemClickListener(this);
		// set adapter
		dispatch = new Dispatch(ApprovalActivity.this);
		arrDispatch = dispatch.getData("http://myapp.freezoy.com/",
				new OnLoadListener() {

					@Override
					public void onStart() {
						loading.setVisibility(View.VISIBLE);
						connectError.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess() {
						loading.setVisibility(View.GONE);
						dispatchAdapter.notifyDataSetChanged();
					}

					@Override
					public void onFalse() {
						loading.setVisibility(View.GONE);
						connectError.setVisibility(View.VISIBLE);
					}
				});
		dispatchAdapter = new DispatchAdapter(ApprovalActivity.this,
				arrDispatch);
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
		case R.id.retry:
			dispatchAdapter.setData(dispatch.getData(
					"http://myapp.freezoy.com/", new OnLoadListener() {

						@Override
						public void onStart() {
							loading.setVisibility(View.VISIBLE);
							connectError.setVisibility(View.GONE);
						}

						@Override
						public void onSuccess() {
							loading.setVisibility(View.GONE);
							// listDispatch.setAdapter(dispatchAdapter);
							dispatchAdapter.notifyDataSetChanged();
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
		startDownload();
		// Intent intent = new Intent(ApprovalActivity.this,
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
		Keyboard.showKeyboard(ApprovalActivity.this, editSearch);
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
				dispatchAdapter.setData(searchData);
				dispatchAdapter.notifyDataSetChanged();
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
		Keyboard.hideKeyboard(ApprovalActivity.this, editSearch);
		searchView.setVisibility(View.GONE);
		editSearch.setText("");
	}

	private void startDownload() {
		dialog = new ProgressDialog(ApprovalActivity.this);
		downloadFile = new DownloadFile("http://office.sinco.pro.vn/public/file/06-GTT-CT-DTN_98193.pdf");
		dialog.setMessage("Dang tai: " + downloadFile.getFileName());
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		if (downloadFile.isZero()) {
			Log.d("TuNT", "isZero");
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setIndeterminate(true);
		} else {
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMax(100);
		}
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					showDialogConfirmCancel();
				}
				return false;
			}
		});
		dialog.show();

		downloadFile.setOnDownloadListener(new OnDownloadListener() {

			@Override
			public void onSuccess(String p) {
				if (dialog.isShowing())
					dialog.dismiss();
				if (aDialog != null) {
					if (aDialog.isShowing())
						aDialog.dismiss();
				}
				downloadFile = null;
				File pdfFile = new File(p);
				try {
					if (pdfFile.exists()) {
						Uri path = Uri.fromFile(pdfFile);
						Intent objIntent = new Intent(Intent.ACTION_VIEW);
						objIntent.setDataAndType(path, "application/pdf");
						objIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(objIntent);
					} else {
						Toast.makeText(ApprovalActivity.this, "File NotFound",
								Toast.LENGTH_SHORT).show();
					}
				} catch (ActivityNotFoundException e) {
					Intent intent = new Intent(ApprovalActivity.this,
							ViewDispatchActivity.class);
					intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, p);
					startActivity(intent);
					Toast.makeText(ApprovalActivity.this,
							"No Viewer Application Found", Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.d("TuNT", "onSuccess: " + p);
			}

			@Override
			public void onFalse() {
				if (dialog.isShowing())
					dialog.dismiss();
				if (aDialog != null) {
					if (aDialog.isShowing())
						aDialog.dismiss();
				}
				Log.d("TuNT", "onFalse");
			}

			@Override
			public void onDownload(int progress) {
				Log.d("TuNT", "onDownload: " + progress);
				dialog.setProgress(progress);
			}

			@Override
			public void onCancel() {
				if (dialog.isShowing())
					dialog.dismiss();
				if (aDialog != null) {
					if (aDialog.isShowing())
						aDialog.dismiss();
				}
			}
		});
	}

	private void showDialogConfirmCancel() {
		if (aDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ApprovalActivity.this);
			builder.setMessage("Ban muon huy???");
			builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					downloadFile.cancel();
					if (ApprovalActivity.this.dialog.isShowing()) {
						ApprovalActivity.this.dialog.dismiss();
					}
					aDialog.dismiss();
				}
			});
			builder.setNegativeButton("Tro ve",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							aDialog.dismiss();
						}
					});
			aDialog = builder.create();
			aDialog.show();
		} else {
			if (!aDialog.isShowing()) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						ApprovalActivity.this);
				builder.setMessage("Ban muon huy???");
				builder.setPositiveButton("Ok",
						new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								downloadFile.cancel();
								if (ApprovalActivity.this.dialog.isShowing()) {
									ApprovalActivity.this.dialog.dismiss();
								}
								aDialog.dismiss();
							}
						});
				builder.setNegativeButton("Tro ve",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								aDialog.dismiss();
							}
						});
				aDialog = builder.create();
				aDialog.show();
			}
		}
	}
}
