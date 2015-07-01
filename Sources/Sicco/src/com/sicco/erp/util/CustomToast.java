package com.sicco.erp.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sicco.erp.R;

public class CustomToast extends Toast {
	Context context;
	TextView txtToastError;

	public CustomToast(Context context) {
		super(context);
		this.context = context;
	}

	public Toast ToastError(String stringError) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.toast_error_custom, null);
		txtToastError = (TextView) view.findViewById(R.id.txtToastError);
		txtToastError.setText(stringError);
		
		final Toast toastError = new Toast(context);
		toastError.setView(view);
		toastError.setDuration(LENGTH_LONG);
		toastError.setGravity(Gravity.TOP, 0, 0);
		
		return toastError;
	}

}
