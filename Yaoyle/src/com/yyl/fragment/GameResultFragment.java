package com.yyl.fragment;


import java.util.HashMap;

import com.yyl.R;
import com.yyl.game.GameActivity;
import com.yyl.mode.GameResultInfo;
import com.yyl.mode.GamerDiceInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameResultFragment extends DialogFragment implements OnClickListener{

	private View view;
	private Context context;
	
	private GameResultInfo resultInfo = new GameResultInfo();

	private HashMap<String, Integer> drawableMap = new HashMap<String, Integer>();
	
	private HashMap<String, Integer> smallMap = new HashMap<String, Integer>();
	
	private TextView diceNum, comScore;
	private ImageView resultPoint, gameResultIcon;
	
	private TextView gamer1name, gamer2name, gamer3name, gamer4name, gamer5name;
	
	private TextView resultState, scoreTitle;
	
	private Button finishFragment;
	
	private BaseFragmentListener listener;
	
	public GameResultFragment(Context context, Bundle b) {
		this.context = context;
		resultInfo = (GameResultInfo) b.getSerializable("result");
	}

	
	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (listener != null) {
			listener.onCallBack(null);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.game_result_fragment, null);
		initDrawableMap();
		initView();
		initContent();
		return view;
	}
	
	private void initView() {
		diceNum = (TextView) view.findViewById(R.id.dice_num);
		resultPoint = (ImageView) view.findViewById(R.id.result_point);
		comScore = (TextView) view.findViewById(R.id.com_score);
		gamer1name = (TextView) view.findViewById(R.id.gamer_1_name);
		gamer2name = (TextView) view.findViewById(R.id.gamer_2_name);
		gamer3name = (TextView) view.findViewById(R.id.gamer_3_name);
		gamer4name = (TextView) view.findViewById(R.id.gamer_4_name);
		gamer5name = (TextView) view.findViewById(R.id.gamer_5_name);
		view.findViewById(R.id.dismiss_dialog).setOnClickListener(this);
		view.findViewById(R.id.share_to_sina).setOnClickListener(this);
		view.findViewById(R.id.share_to_wx).setOnClickListener(this);
		finishFragment = (Button) view.findViewById(R.id.finish_fragment);
		finishFragment.setOnClickListener(this);
		resultState = (TextView) view.findViewById(R.id.result_state);
		scoreTitle = (TextView) view.findViewById(R.id.score_title);
		gameResultIcon = (ImageView) view.findViewById(R.id.game_result_icon);
	}
	
	private void initContent() {
		if (resultInfo == null) return;
		for (int i=0; i < resultInfo.diceList.size(); i++) {
			showGamerResultItem(i, resultInfo.diceList.get(i));
		}
		if (!TextUtils.isEmpty(resultInfo.diceNum) && !TextUtils.isEmpty(resultInfo.dicePoint)) {
			diceNum.setText(resultInfo.diceNum);
			resultPoint.setBackgroundResource(drawableMap.get(resultInfo.dicePoint));
		}
		if (resultInfo.win.equals("1")) {
			resultState.setText(getString(R.string.you_win_note));
			scoreTitle.setText(getString(R.string.congratulate_get));
			finishFragment.setText(getString(R.string.follow_up_play));
			gameResultIcon.setBackgroundResource(R.drawable.smile_result);
			comScore.setText("+" + resultInfo.score);
		} else if (resultInfo.win.equals("2")) {
			resultState.setText(getString(R.string.you_loose_note));
			scoreTitle.setText(getString(R.string.incaution_loose));
			finishFragment.setText(getString(R.string.make_persistent_play));
			gameResultIcon.setBackgroundResource(R.drawable.cry_result);
			comScore.setText(resultInfo.score);
		}
	}
	
	private void initDrawableMap() {
		drawableMap.put("1", R.drawable.bs_1);
		drawableMap.put("2", R.drawable.bs_2);
		drawableMap.put("3", R.drawable.bs_3);
		drawableMap.put("4", R.drawable.bs_4);
		drawableMap.put("5", R.drawable.bs_5);
		drawableMap.put("6", R.drawable.bs_6);
		smallMap.put("1", R.drawable.ms_1);
		smallMap.put("2", R.drawable.ms_2);
		smallMap.put("3", R.drawable.ms_3);
		smallMap.put("4", R.drawable.ms_4);
		smallMap.put("5", R.drawable.ms_5);
		smallMap.put("6", R.drawable.ms_6);
	}

	private void showResultDetail(LinearLayout layout, GamerDiceInfo diceInfo) {
		View item = View.inflate(context, R.layout.game_result_item, null);
		ImageView img1 = (ImageView) item.findViewById(R.id.gamer_dice_1);
		ImageView img2 = (ImageView) item.findViewById(R.id.gamer_dice_2);
		ImageView img3 = (ImageView) item.findViewById(R.id.gamer_dice_3);
		ImageView img4 = (ImageView) item.findViewById(R.id.gamer_dice_4);
		ImageView img5 = (ImageView) item.findViewById(R.id.gamer_dice_5);
		String[] array = diceInfo.dice.split("-");
		img1.setBackgroundResource(smallMap.get(array[0]));
		img2.setBackgroundResource(smallMap.get(array[1]));
		img3.setBackgroundResource(smallMap.get(array[2]));
		img4.setBackgroundResource(smallMap.get(array[3]));
		img5.setBackgroundResource(smallMap.get(array[4]));
		layout.addView(item);
	}

	private void showGamerResultItem(int gamer, GamerDiceInfo diceInfo) {
		switch (gamer) {
		case 0:
			LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.gamer_1_layout);
			if (layout1 != null) {
				layout1.setVisibility(View.VISIBLE);
				gamer1name.setText(diceInfo.userName);
				showResultDetail(layout1, diceInfo);
			}
			break;
		case 1:
			view.findViewById(R.id.gamer_2_layout).setVisibility(View.VISIBLE);
			LinearLayout layout2 = (LinearLayout) view.findViewById(R.id.gamer_2_layout);
			if (layout2 != null) {
				layout2.setVisibility(View.VISIBLE);
				gamer2name.setText(diceInfo.userName);
				showResultDetail(layout2, diceInfo);
			}
			break;
		case 2:
			view.findViewById(R.id.gamer_3_layout).setVisibility(View.VISIBLE);
			LinearLayout layout3 = (LinearLayout) view.findViewById(R.id.gamer_3_layout);
			if (layout3 != null) {
				layout3.setVisibility(View.VISIBLE);
				gamer3name.setText(diceInfo.userName);
				showResultDetail(layout3, diceInfo);
			}
			break;
		case 3:
			view.findViewById(R.id.gamer_4_layout).setVisibility(View.VISIBLE);
			LinearLayout layout4 = (LinearLayout) view.findViewById(R.id.gamer_4_layout);
			if (layout4 != null) {
				layout4.setVisibility(View.VISIBLE);
				gamer4name.setText(diceInfo.userName);
				showResultDetail(layout4, diceInfo);
			}
			break;
		case 4:
			view.findViewById(R.id.gamer_5_layout).setVisibility(View.VISIBLE);
			LinearLayout layout5 = (LinearLayout) view.findViewById(R.id.gamer_5_layout);
			if (layout5 != null) {
				layout5.setVisibility(View.VISIBLE);
				gamer5name.setText(diceInfo.userName);
				showResultDetail(layout5, diceInfo);
			}
			break;

		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dismiss_dialog:
			dismissAllowingStateLoss();
			break;

		case R.id.share_to_sina:
			if (context instanceof GameActivity) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						((GameActivity) context).shareToSinaWb();
					}
				}).start();
			}
			break;
			
		case R.id.share_to_wx:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					((GameActivity) context).shareToWX();
				}
			}).start();
			break;
			
		case R.id.finish_fragment:
			dismissAllowingStateLoss();
			break;
		default:
			break;
		}
		
	}
}
