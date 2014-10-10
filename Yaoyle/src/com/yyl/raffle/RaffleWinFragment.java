package com.yyl.raffle;

import com.yyl.R;
import com.yyl.mode.LotteryInfo;
import com.yyl.utils.ImageOperater;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RaffleWinFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private Context context;
	
	private ImageView rafflePic;
	private TextView raffleWinTx;
	
	private Button getRaffleBtn;
	
	private LotteryInfo prizeInfo = new LotteryInfo();
	
	public RaffleWinFragment(Context context, Bundle b) {
		this.context = context;
		if (b != null) {
			prizeInfo = (LotteryInfo) b.getSerializable("prize_info");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.raffle_win_fragment, null);
		initView();
		initContent();
		return view;
	}
	
	private void initView() {
		view.findViewById(R.id.goto_get_raflle_btn).setOnClickListener(this);
		rafflePic = (ImageView) view.findViewById(R.id.raffle_pic);
		raffleWinTx = (TextView) view.findViewById(R.id.raffle_win_tx);
		getRaffleBtn = (Button) view.findViewById(R.id.goto_get_raflle_btn);
	}

	private void initContent() {
		if (prizeInfo != null && !TextUtils.isEmpty(prizeInfo.name)) {
			raffleWinTx.setText(getString(R.string.raffle_win_note).replaceAll("%", prizeInfo.name));
		}
		if (prizeInfo != null && !TextUtils.isEmpty(prizeInfo.photo)) {
			ImageOperater.getInstance(context).onLoadImage(prizeInfo.photo, rafflePic);
		}
		if (prizeInfo != null && prizeInfo.type.equals("1")) {
			getRaffleBtn.setText(getString(R.string.press_coin_btn));
		} else {
			getRaffleBtn.setText(getString(R.string.press_get_prize));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_get_raflle_btn:
			dismissAllowingStateLoss();
			break;

		default:
			break;
		}
		
	}

}
