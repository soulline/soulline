package com.cdd.base;

import com.cdd.R;
import com.cdd.activity.login.LoginActivity;
import com.cdd.base.BaseActivity;
import com.cdd.fragment.AlarmFragment;
import com.cdd.fragment.BaseFragmentListener;
import com.cdd.fragment.CommunityFragment;
import com.cdd.fragment.FindFragment;
import com.cdd.fragment.MineFragment;
import com.cdd.fragment.SignSuccessFragment;
import com.cdd.mode.LoginEntry;
import com.cdd.net.RequestListener;
import com.cdd.operater.LoginOperater;
import com.cdd.util.CddRequestCode;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener {

	private ViewPager mainViewPager;

	private int index = 0;

	private ImageView titleCommunityImg, titleFindImg,
			titleMineImg;

	private TextView communityTx, findTx, mineTx;

	private CommunityFragment communityF;

	private AlarmFragment alarmF;

	private FindFragment findF;

	private MineFragment mineF;
	
	private int backCount = 0;
	
	private int loginRequestCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK
				&& requestCode == CddRequestCode.LOGIN_REQUEST) {
		} else if (resultCode == RESULT_CANCELED
				&& requestCode == CddRequestCode.LOGIN_REQUEST) {
		} else if (resultCode == RESULT_OK
				&& requestCode == CddRequestCode.MINE_LOGIN_REQUEST) {
			index = 2;
		} else if (resultCode == RESULT_OK
				&& requestCode == CddRequestCode.REMIND_LOGIN_REQUEST) {
//			index = 1;
		} else if (requestCode == CddRequestCode.ACTION_LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
			
		}
	}
	
	private void showTipNoteDialog(final String msg) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				Builder b = new Builder(context);
				b.setMessage(msg);
				b.setNegativeButton(getString(R.string.action_cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				b.setPositiveButton(R.string.action_sure,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								finish();
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (backCount == 1) {
			showTipNoteDialog("确定要退出财叮当吗？");
		} else {
			backCount++;
			showToast("再按一次返回键退出");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		backCount = 0;
		if (/*(loginRequestCode == 1 && app.isLogin()) 
				|| */(loginRequestCode == 2 && app.isLogin())) {
			index = loginRequestCode;
		}
		mainViewPager.setCurrentItem(index);
		setCheckTitle(index);
		initFragmentContent(index);
	}

	private void initFragmentContent(int type) {
		switch (type) {
		case 0:
			if (communityF != null) {
				communityF.initContent();
				communityF.showTitleLayout();
			}
			break;

/*		case 1:
			if (alarmF != null) {
				alarmF.initContent();
			}
			break;*/

		case 1:
			if (findF != null) {
				findF.initContent();
			}
			break;

		case 2:
			if (mineF != null) {
				mineF.initContent();
			}
			break;

		default:
			break;
		}
	}

	private void initView() {
		findViewById(R.id.community_title_layout).setOnClickListener(this);
//		findViewById(R.id.alarm_title_layout).setOnClickListener(this);
		findViewById(R.id.find_title_layout).setOnClickListener(this);
		findViewById(R.id.mine_title_layout).setOnClickListener(this);

		titleCommunityImg = (ImageView) findViewById(R.id.title_community_img);
//		titleAlarmImg = (ImageView) findViewById(R.id.title_alarm_img);
		titleFindImg = (ImageView) findViewById(R.id.title_find_img);
		titleMineImg = (ImageView) findViewById(R.id.title_mine_img);

		communityTx = (TextView) findViewById(R.id.community_tx);
//		alarmTx = (TextView) findViewById(R.id.alarm_tx);
		findTx = (TextView) findViewById(R.id.find_tx);
		mineTx = (TextView) findViewById(R.id.mine_tx);

		mainViewPager = (ViewPager) findViewById(R.id.main_view_pager);
		mainViewPager.setAdapter(new SectionsPagerAdapter(manager));
		mainViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == 2 && !app.isLogin()) {
					Intent login = new Intent(context, LoginActivity.class);
					startActivityForResult(login,
							CddRequestCode.MINE_LOGIN_REQUEST);
					setCheckTitle(index);
					mainViewPager.setCurrentItem(index);
				} /*else if (position == 1 && !app.isLogin()) {
					Intent login = new Intent(context, LoginActivity.class);
					startActivityForResult(login,
							CddRequestCode.REMIND_LOGIN_REQUEST);
					setCheckTitle(index);
					mainViewPager.setCurrentItem(index);
				}*/ else {
					index = position;
					setCheckTitle(index);
					initFragmentContent(index);
				}

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
		mainViewPager.setCurrentItem(index);
		setCheckTitle(index);
	}

	private void setCheckTitle(int index) {
		switch (index) {
		case 0:
			titleCommunityImg
					.setBackgroundResource(R.drawable.title_community_choose);
//			titleAlarmImg.setBackgroundResource(R.drawable.title_alarm);
			titleFindImg.setBackgroundResource(R.drawable.title_find);
			titleMineImg.setBackgroundResource(R.drawable.title_mine);
			communityTx.setTextColor(getResources().getColor(
					R.color.bottom_title_txblue));
//			alarmTx.setTextColor(getResources().getColor(R.color.white));
			findTx.setTextColor(getResources().getColor(R.color.white));
			mineTx.setTextColor(getResources().getColor(R.color.white));
			break;
/*		case 1:
			titleCommunityImg.setBackgroundResource(R.drawable.title_community);
//			titleAlarmImg.setBackgroundResource(R.drawable.title_alarm_choose);
			titleFindImg.setBackgroundResource(R.drawable.title_find);
			titleMineImg.setBackgroundResource(R.drawable.title_mine);
			communityTx.setTextColor(getResources().getColor(R.color.white));
//			alarmTx.setTextColor(getResources().getColor(
//					R.color.bottom_title_txblue));
			findTx.setTextColor(getResources().getColor(R.color.white));
			mineTx.setTextColor(getResources().getColor(R.color.white));
			break;*/
		case 1:
			titleCommunityImg.setBackgroundResource(R.drawable.title_community);
//			titleAlarmImg.setBackgroundResource(R.drawable.title_alarm);
			titleFindImg.setBackgroundResource(R.drawable.title_find_choose);
			titleMineImg.setBackgroundResource(R.drawable.title_mine);
			communityTx.setTextColor(getResources().getColor(R.color.white));
//			alarmTx.setTextColor(getResources().getColor(R.color.white));
			findTx.setTextColor(getResources().getColor(
					R.color.bottom_title_txblue));
			mineTx.setTextColor(getResources().getColor(R.color.white));
			break;
		case 2:
			titleCommunityImg.setBackgroundResource(R.drawable.title_community);
//			titleAlarmImg.setBackgroundResource(R.drawable.title_alarm);
			titleFindImg.setBackgroundResource(R.drawable.title_find);
			titleMineImg.setBackgroundResource(R.drawable.title_mine_choose);
			communityTx.setTextColor(getResources().getColor(R.color.white));
//			alarmTx.setTextColor(getResources().getColor(R.color.white));
			findTx.setTextColor(getResources().getColor(R.color.white));
			mineTx.setTextColor(getResources().getColor(
					R.color.bottom_title_txblue));
			break;

		default:
			break;
		}
	}

	public void displayFragment(boolean isOpen, String tag, Bundle bundle,
			BaseFragmentListener listener) {
		if (isOpen) {
			showInFragment(tag, -1, createFragment(tag, bundle, listener));
		} else {
			closeInFragment(tag);
		}
	}

	public DialogFragment createFragment(final String tag, Bundle b,
			BaseFragmentListener listener) {
		if (tag.equals("sign_success")) {
			SignSuccessFragment signFragment = new SignSuccessFragment(context,
					b);
			return signFragment;
		}
		return null;
	}

	public void closeInFragment(final String tag) {
		if (this.isFinishing()) {
			return;
		}
		FragmentTransaction ft = manager.beginTransaction();
		Fragment prev = manager.findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev).commitAllowingStateLoss();
		}
		if (manager.getBackStackEntryCount() > 0) {
			manager.popBackStackImmediate();
		}
	}

	public void showInFragment(String tag, int layoutId, Fragment fragment) {
		if (this.isFinishing()) {
			return;
		}
		if (fragment == null || TextUtils.isEmpty(tag)) {
			return;
		}
		FragmentTransaction ft = manager.beginTransaction();
		Fragment prev = manager.findFragmentByTag(tag);
		if (prev != null) {
			ft.remove(prev);
		}
		// ft.addToBackStack(null);
		ft.replace(layoutId <= 0 ? R.id.base_extra_layout : layoutId, fragment,
				tag).commitAllowingStateLoss();
	}

	public void showSignSuccessFragment() {
		displayFragment(true, "sign_success", null, new BaseFragmentListener() {

			@Override
			public void onCallBack(Object object) {
				// TODO Auto-generated method stub

			}
		});
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				closeInFragment("sign_success");

			}
		}, 1500);
	}

	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				communityF = new CommunityFragment();
				return communityF;
			}/* else if (position == 1) {
				alarmF = new AlarmFragment();
				return alarmF;
			} */else if (position == 1) {
				findF = new FindFragment();
				return findF;
			} else {
				mineF = new MineFragment();
				return mineF;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.community_title_layout:
			index = 0;
			setCheckTitle(0);
			break;
		/*case R.id.alarm_title_layout:
			if (!app.isLogin()) {
				loginRequestCode = 1;
				Intent login = new Intent(context, LoginActivity.class);
				startActivityForResult(login, CddRequestCode.REMIND_LOGIN_REQUEST);
				setCheckTitle(index);
				mainViewPager.setCurrentItem(index);
			} else {
				index = 1;
				setCheckTitle(1);
			}
			break;*/
		case R.id.find_title_layout:
			index = 1;
			setCheckTitle(1);
			break;
		case R.id.mine_title_layout:
			if (!app.isLogin()) {  
				loginRequestCode = 2;
				Intent login = new Intent(context, LoginActivity.class);
				startActivityForResult(login, CddRequestCode.MINE_LOGIN_REQUEST);
				setCheckTitle(index);
				mainViewPager.setCurrentItem(index);
			} else {
				index = 2;
				setCheckTitle(2);
			}
			break;

		default:
			break;
		}
		mainViewPager.setCurrentItem(index);
	}
}
