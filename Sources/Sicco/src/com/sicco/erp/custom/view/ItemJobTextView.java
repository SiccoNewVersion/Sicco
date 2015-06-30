package com.sicco.erp.custom.view;

import com.sicco.erp.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemJobTextView extends LinearLayout {
	
	private TextView txtLabel = null;
//	private TextView txtContent = null;
	
	public ItemJobTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(inflater != null){
			inflater.inflate(R.layout.custom_item_job_textview, this);
			
			txtLabel = (TextView)findViewById(R.id.txtLabel);
//			txtContent = (TextView)findViewById(R.id.txtContent);
		
			CharSequence charLabel = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "label_item");
//			CharSequence charContent = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "content_item");
		
			this.txtLabel.setText(charLabel);
//			this.txtContent.setText(charContent);
		}
	}
	
}