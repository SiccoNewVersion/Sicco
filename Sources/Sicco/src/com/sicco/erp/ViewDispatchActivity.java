package com.sicco.erp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sicco.erp.view.ExtendedViewPager;
import com.sicco.erp.view.TouchImageView;

public class ViewDispatchActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_dispatch);
		ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(new TouchImageAdapter());
	}

	static class TouchImageAdapter extends PagerAdapter {

		private static int[] images = { R.drawable.nature_1,
				R.drawable.nature_2, R.drawable.nature_3, R.drawable.nature_4,
				R.drawable.nature_5 };

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			TouchImageView img = new TouchImageView(container.getContext());
			img.setImageResource(images[position]);
			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
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
}
