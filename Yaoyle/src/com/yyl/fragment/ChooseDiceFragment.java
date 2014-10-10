package com.yyl.fragment;

import java.util.HashMap;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.mode.GamerGessInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChooseDiceFragment extends DialogFragment implements OnClickListener{

	private View view;
	private Context context;
	
	private LinearLayout selfAddCountLayout;
	
	private int GROUP_NUMBER = 0;
	
	private int TOTAL_COUNT = 25;
	
	private int CEN_CHOOSE = 0;
	
	private int DICE_CHOOSE = 0;
	
	private GamerGessInfo lastGess = new GamerGessInfo();
	
	private ImageView gessDice1, gessDice2, 
	gessDice3, gessDice4, gessDice5, gessDice6, gamerDiceNum;
	
	private TextView gamerDiceCount;
	
	private BaseFragmentListener listener;
	
	private HashMap<String, Integer> drawMap = new HashMap<String, Integer>();
	
	private float density = 0.0f;
	
	public ChooseDiceFragment(Context context, Bundle b) {
		this.context = context;
		GROUP_NUMBER = b.getInt("group_num");
		lastGess = (GamerGessInfo) b.getSerializable("last_gess");
		DisplayMetrics metric = new DisplayMetrics();
		((BaseActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		density = metric.density;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.choose_dice_layout, null);
		initView();
		initDrawMap();
		initContent();
		return view;
	}
	
	private void initDrawMap() {
		drawMap.put("1", R.drawable.super_ds_1);
		drawMap.put("2", R.drawable.super_ds_2);
		drawMap.put("3", R.drawable.super_ds_3);
		drawMap.put("4", R.drawable.super_ds_4);
		drawMap.put("5", R.drawable.super_ds_5);
		drawMap.put("6", R.drawable.super_ds_6);
	}
	
	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}
	
	private void initContent() {
		computeCount(GROUP_NUMBER, TOTAL_COUNT);
		if (lastGess != null && !TextUtils.isEmpty(lastGess.preDiceNum) 
				&& !TextUtils.isEmpty(lastGess.preDicePoint)) {
			view.findViewById(R.id.last_gess_layout).setVisibility(View.VISIBLE);
			gamerDiceCount.setText(lastGess.preDiceNum);
			gamerDiceNum.setBackgroundResource(drawMap.get(lastGess.preDicePoint));
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (listener != null) {
			listener.onCallBack(null);
		}
	}

	private void initView() {
		selfAddCountLayout = (LinearLayout) view.findViewById(R.id.self_add_count_layout);
		gessDice1 = (ImageView) view.findViewById(R.id.self_choose_1);
		gessDice2 = (ImageView) view.findViewById(R.id.self_choose_2);
		gessDice3 = (ImageView) view.findViewById(R.id.self_choose_3);
		gessDice4 = (ImageView) view.findViewById(R.id.self_choose_4);
		gessDice5 = (ImageView) view.findViewById(R.id.self_choose_5);
		gessDice6 = (ImageView) view.findViewById(R.id.self_choose_6);
		gessDice1.setOnClickListener(this);
		gessDice2.setOnClickListener(this);
		gessDice3.setOnClickListener(this);
		gessDice4.setOnClickListener(this);
		gessDice5.setOnClickListener(this);
		gessDice6.setOnClickListener(this);
		view.findViewById(R.id.left_move).setOnClickListener(this);
		view.findViewById(R.id.right_move).setOnClickListener(this);
		gamerDiceCount = (TextView) view.findViewById(R.id.gamer_dice_count);
		gamerDiceNum = (ImageView) view.findViewById(R.id.gamer_dice_num);
	}

	
	private void clearChooseHistory() {
		gessDice1.setBackgroundResource(R.drawable.bs_1);
		gessDice2.setBackgroundResource(R.drawable.bs_2);
		gessDice3.setBackgroundResource(R.drawable.bs_3);
		gessDice4.setBackgroundResource(R.drawable.bs_4);
		gessDice5.setBackgroundResource(R.drawable.bs_5);
		gessDice6.setBackgroundResource(R.drawable.bs_6);
	}
	
	
	
	private void computeCount(int goupNum, int totalCount) {
		selfAddCountLayout.removeAllViews();
		int total = 0;
		int start = 2;
		if (goupNum > 0) {
			start = goupNum * 5;
		}
		for (int i = start; i < totalCount; i++) {
			if (total == 5) {
				break;
			}
			String count = i + "";
			if (total != 4) {
				addCountItem(false, count, total + 1);
			} else {
				addCountItem(true, count, total + 1);
			}
			total++;
		}
	}
	
	private void checkDiceGess() {
		if (CEN_CHOOSE > 0 && DICE_CHOOSE > 0) {
			String diceInfo = CEN_CHOOSE + "-" + DICE_CHOOSE;
			if (listener != null) {
				listener.onCallBack(diceInfo);
			}
		}
	}
	
	private void addCountItem(boolean isLastItem, String numer, int itemId) {
		final TextView txtV = new TextView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				getResources().getDimensionPixelSize(R.dimen.dd_dimen_58px),
				getResources().getDimensionPixelSize(R.dimen.dd_dimen_58px));
		if (!isLastItem) {
			lp.setMargins(0, 0, (int) getResources().getDimensionPixelSize(R.dimen.dd_dimen_10px), 0);
		}
		txtV.setLayoutParams(lp);
		txtV.setGravity(Gravity.CENTER);
		txtV.setBackgroundResource(R.drawable.block);
		if (density > 2.0f) {
			txtV.setTextSize(getResources().getDimensionPixelSize(R.dimen.dd_dimen_15px));
		} else {
			txtV.setTextSize(getResources().getDimensionPixelSize(R.dimen.dd_dimen_22px));
		}
		txtV.setTextColor(getResources().getColor(R.color.white));
		txtV.setTag(itemId);
		txtV.setText(numer);
		selfAddCountLayout.addView(txtV);
		txtV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CEN_CHOOSE = Integer.valueOf(txtV.getText().toString());
				txtV.setBackgroundResource(R.drawable.block_gray);
				for (int i=1; i < 6; i++) {
					TextView newTx = (TextView) selfAddCountLayout.findViewWithTag(i);
					if (newTx != null && !newTx.getText().equals(txtV.getText())) {
						newTx.setBackgroundResource(R.drawable.block);
					}
				}
				checkDiceGess();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.self_choose_1:
			DICE_CHOOSE = 1;
			gessDice1.setBackgroundResource(R.drawable.super_ds_1_gray);
			gessDice2.setBackgroundResource(R.drawable.super_ds_2);
			gessDice3.setBackgroundResource(R.drawable.super_ds_3);
			gessDice4.setBackgroundResource(R.drawable.super_ds_4);
			gessDice5.setBackgroundResource(R.drawable.super_ds_5);
			gessDice6.setBackgroundResource(R.drawable.super_ds_6);
			checkDiceGess();
			break;
		case R.id.self_choose_2:
			DICE_CHOOSE = 2;
			gessDice1.setBackgroundResource(R.drawable.super_ds_1);
			gessDice2.setBackgroundResource(R.drawable.super_ds_2_gray);
			gessDice3.setBackgroundResource(R.drawable.super_ds_3);
			gessDice4.setBackgroundResource(R.drawable.super_ds_4);
			gessDice5.setBackgroundResource(R.drawable.super_ds_5);
			gessDice6.setBackgroundResource(R.drawable.super_ds_6);
			checkDiceGess();
			break;
		case R.id.self_choose_3:
			DICE_CHOOSE = 3;
			gessDice1.setBackgroundResource(R.drawable.super_ds_1);
			gessDice2.setBackgroundResource(R.drawable.super_ds_2);
			gessDice3.setBackgroundResource(R.drawable.super_ds_3_gray);
			gessDice4.setBackgroundResource(R.drawable.super_ds_4);
			gessDice5.setBackgroundResource(R.drawable.super_ds_5);
			gessDice6.setBackgroundResource(R.drawable.super_ds_6);
			checkDiceGess();
			break;
		case R.id.self_choose_4:
			DICE_CHOOSE = 4;
			gessDice1.setBackgroundResource(R.drawable.super_ds_1);
			gessDice2.setBackgroundResource(R.drawable.super_ds_2);
			gessDice3.setBackgroundResource(R.drawable.super_ds_3);
			gessDice4.setBackgroundResource(R.drawable.super_ds_4_gray);
			gessDice5.setBackgroundResource(R.drawable.super_ds_5);
			gessDice6.setBackgroundResource(R.drawable.super_ds_6);
			checkDiceGess();
			break;
		case R.id.self_choose_5:
			DICE_CHOOSE = 5;
			gessDice1.setBackgroundResource(R.drawable.super_ds_1);
			gessDice2.setBackgroundResource(R.drawable.super_ds_2);
			gessDice3.setBackgroundResource(R.drawable.super_ds_3);
			gessDice4.setBackgroundResource(R.drawable.super_ds_4);
			gessDice5.setBackgroundResource(R.drawable.super_ds_5_gray);
			gessDice6.setBackgroundResource(R.drawable.super_ds_6);
			checkDiceGess();
			break;
		case R.id.self_choose_6:
			DICE_CHOOSE = 6;
			gessDice1.setBackgroundResource(R.drawable.super_ds_1);
			gessDice2.setBackgroundResource(R.drawable.super_ds_2);
			gessDice3.setBackgroundResource(R.drawable.super_ds_3);
			gessDice4.setBackgroundResource(R.drawable.super_ds_4);
			gessDice5.setBackgroundResource(R.drawable.super_ds_5);
			gessDice6.setBackgroundResource(R.drawable.super_ds_6_gray);
			checkDiceGess();
			break;

		case R.id.left_move:
			if (GROUP_NUMBER != 0) {
				GROUP_NUMBER--;
				computeCount(GROUP_NUMBER, TOTAL_COUNT);
			} else {
				return;
			}
			break;
		case R.id.right_move:
			if (GROUP_NUMBER != ((TOTAL_COUNT / 5) - 1)) {
				GROUP_NUMBER++;
				computeCount(GROUP_NUMBER, TOTAL_COUNT);
			} else {
				return;
			}
			break;
		default:
			break;
		}
		
	}
}
