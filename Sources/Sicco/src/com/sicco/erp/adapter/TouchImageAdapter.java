package com.sicco.erp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.sicco.erp.R;
import com.sicco.erp.view.TouchImageView;

public class TouchImageAdapter extends PagerAdapter {
	private Context context;
	private static int[] images = { R.drawable.nature_1, R.drawable.nature_2,
			R.drawable.nature_3, R.drawable.nature_4, R.drawable.nature_5 };

	public TouchImageAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		TouchImageView img = new TouchImageView(container.getContext());
		Glide.with(context)
				.load(images[position])
				.placeholder(R.drawable.image_loading)
				.error(R.drawable.image_error)
				.crossFade()
				.into(img);
		container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		// ExtendedViewPager pager = (ExtendedViewPager) container;
		// View view = getView(position, pager);
		//
		// pager.addView(view);
		return img;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
