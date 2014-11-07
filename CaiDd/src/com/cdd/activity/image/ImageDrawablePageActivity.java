package com.cdd.activity.image;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.DrawableEntry;
import com.cdd.util.BitmapUtil;

public class ImageDrawablePageActivity extends BaseActivity implements OnClickListener{

	private ArrayList<String> darawableList = new ArrayList<String>();
	
	private int currentPosition = 0;
	
	private ZCPagerAdapter pa;
	private ViewPager viewPage;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pictures);
		initView();
		initContent();
	}

	private void initView() {
		findViewById(R.id.close_btn).setOnClickListener(this);
	}

	
	public class ZCPagerAdapter extends PagerAdapter{

		private Context ctx;
		
		private ArrayList<Drawable> list = new ArrayList<Drawable>();
		
		public ZCPagerAdapter(Context ctx) {
			this.ctx = ctx;
		}
		
		public void setData(ArrayList<Drawable> list) {
			this.list = list;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container,
				final int position) {
			View v = LayoutInflater.from(ctx).inflate(
					R.layout.image_load_layout, null);
			final ImageView iv = (ImageView) v.findViewById(R.id.img);
			iv.setImageResource(R.drawable.default_ad_icon);
			iv.setOnTouchListener(new ImageTouchListener(iv));
			iv.setImageDrawable(list.get(position));
			container.addView(v);
			return v;
		}

		
	}

	private void initCountTitle(int position, int size) {
		int select = position + 1;
		String title = select + "/" + size;
		initTitle(title);
	}
	
	private void loadImages(ArrayList<Drawable> list) {
		viewPage = (ViewPager) findViewById(R.id.image_pager);
		pa = new ZCPagerAdapter(context);
		pa.setData(list);
		viewPage.setAdapter(pa);
		viewPage.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				currentPosition = position;
				initCountTitle(position, darawableList.size());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		currentPosition = getIntent().getIntExtra("image_index", -1);
		if (currentPosition != -1) {
			viewPage.setCurrentItem(currentPosition);
		}
	}

	private void initContent() {
		darawableList = (ArrayList<String>) getIntent().getSerializableExtra("drawable_list");
		if (darawableList != null && darawableList.size() > 0) {
			String title = 1 + "/" + darawableList.size();
			initTitle(title);
			showLoading(true);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					loadBitmapList();
				}
			}).start();
		}
	}
	
	private void loadBitmapList() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		final ArrayList<Drawable> list = new ArrayList<Drawable>();
		for (String path : darawableList) {
			Bitmap bitmap = BitmapUtil.getBitmapByPath(path, options, 894,
					595);
			BitmapDrawable portraitD = new BitmapDrawable(bitmap);
			list.add(portraitD);
		}
		showLoading(false);
		if (list.size() > 0) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					loadImages(list);
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			
		case R.id.close_btn:
			finish();
			break;

		default:
			break;
		}
		
	}


}
