package com.yyl.raffle;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyl.BaseActivity;
import com.yyl.R;
import com.yyl.fragment.BaseFragmentListener;
import com.yyl.mode.GetPrize;
import com.yyl.mode.LotteryInfo;
import com.yyl.mode.PrizeInfo;
import com.yyl.mode.RaffleInfo;
import com.yyl.net.RequestListener;
import com.yyl.operater.GetRaffleOperater;
import com.yyl.operater.PrizeListOperater;
import com.yyl.operater.PrizeLotteryOperater;
import com.yyl.utils.ImageOperater;

public class RaffleActivity extends BaseActivity implements OnClickListener {

	private RaffleInfo raffInfo = new RaffleInfo();

	private ImageView rafflePic1, rafflePic2, rafflePic3, rafflePic4, rafflePic5, rafflePic6, rafflePic7,
	        rafflePic8, rafflePic9, rafflePic10;

	private TextView raffleTxt1, raffleTxt2, raffleTxt3, raffleTxt4, raffleTxt5, raffleTxt6, raffleTxt7,
	        raffleTxt8, raffleTxt9, raffleTxt10;
	
	private LinearLayout raffleBoxLayout1, raffleBoxLayout2, raffleBoxLayout3, raffleBoxLayout4, raffleBoxLayout5,
	        raffleBoxLayout6, raffleBoxLayout7, raffleBoxLayout8, raffleBoxLayout9, raffleBoxLayout10;

	private Button raffleDo;

	private EditText rafflePhoneEdit, raffleAddressEdit;

	private LotteryInfo lottery = new LotteryInfo();

	private boolean isRaffleLoose = false;

	private boolean isGetLottery = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.raffle_activity);
		setFullScreen();
		initView();
		requestPrize();
		setSmallSize();
	}

	private void initView() {
		rafflePic1 = (ImageView) findViewById(R.id.raffle_pic_1);
		rafflePic2 = (ImageView) findViewById(R.id.raffle_pic_2);
		rafflePic3 = (ImageView) findViewById(R.id.raffle_pic_3);
		rafflePic4 = (ImageView) findViewById(R.id.raffle_pic_4);
		rafflePic5 = (ImageView) findViewById(R.id.raffle_pic_5);
		rafflePic6 = (ImageView) findViewById(R.id.raffle_pic_6);
		rafflePic7 = (ImageView) findViewById(R.id.raffle_pic_7);
		rafflePic8 = (ImageView) findViewById(R.id.raffle_pic_8);
		rafflePic9 = (ImageView) findViewById(R.id.raffle_pic_9);
		rafflePic10 = (ImageView) findViewById(R.id.raffle_pic_10);
		raffleTxt1 = (TextView) findViewById(R.id.raffle_txt_1);
		raffleTxt2 = (TextView) findViewById(R.id.raffle_txt_2);
		raffleTxt3 = (TextView) findViewById(R.id.raffle_txt_3);
		raffleTxt4 = (TextView) findViewById(R.id.raffle_txt_4);
		raffleTxt5 = (TextView) findViewById(R.id.raffle_txt_5);
		raffleTxt6 = (TextView) findViewById(R.id.raffle_txt_6);
		raffleTxt7 = (TextView) findViewById(R.id.raffle_txt_7);
		raffleTxt8 = (TextView) findViewById(R.id.raffle_txt_8);
		raffleTxt9 = (TextView) findViewById(R.id.raffle_txt_9);
		raffleTxt10 = (TextView) findViewById(R.id.raffle_txt_10);
		
		raffleBoxLayout1 = (LinearLayout) findViewById(R.id.raffle_box_layout_1);
		raffleBoxLayout2 = (LinearLayout) findViewById(R.id.raffle_box_layout_2);
		raffleBoxLayout3 = (LinearLayout) findViewById(R.id.raffle_box_layout_3);
		raffleBoxLayout4 = (LinearLayout) findViewById(R.id.raffle_box_layout_4);
		raffleBoxLayout5 = (LinearLayout) findViewById(R.id.raffle_box_layout_5);
		raffleBoxLayout6 = (LinearLayout) findViewById(R.id.raffle_box_layout_6);
		raffleBoxLayout7 = (LinearLayout) findViewById(R.id.raffle_box_layout_7);
		raffleBoxLayout8 = (LinearLayout) findViewById(R.id.raffle_box_layout_8);
		raffleBoxLayout9 = (LinearLayout) findViewById(R.id.raffle_box_layout_9);
		raffleBoxLayout10 = (LinearLayout) findViewById(R.id.raffle_box_layout_10);
		
		
		rafflePhoneEdit = (EditText) findViewById(R.id.raffle_phone_edit);
		raffleAddressEdit = (EditText) findViewById(R.id.raffle_address_edit);
		raffleDo = (Button) findViewById(R.id.raffle_do);
		raffleDo.setOnClickListener(this);
		findViewById(R.id.get_raffle).setOnClickListener(this);
		findViewById(R.id.giveup_raffle).setOnClickListener(this);
	}

	private void setSmallSize() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density = metric.density;
		int with = metric.widthPixels;
		Button get = (Button) findViewById(R.id.get_raffle);
		Button giveUp = (Button) findViewById(R.id.giveup_raffle);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
				getResources().getDimensionPixelSize(R.dimen.dd_dimen_200px),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
				getResources().getDimensionPixelSize(R.dimen.dd_dimen_200px),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp2.setMargins(0, (int) getResources().getDimensionPixelSize(R.dimen.dd_dimen_30px), 0, 0);
		if (density < 2.0f) {
			get.setLayoutParams(lp1);
			giveUp.setLayoutParams(lp2);
		} else if (density == 2.0f && with < 1280) {
			get.setLayoutParams(lp1);
			giveUp.setLayoutParams(lp2);
		}
	}

	private void loadPrizeInfo() {
		for (int i = 0; i < raffInfo.prizeList.size(); i++) {
			PrizeInfo info = raffInfo.prizeList.get(i);
			String url = info.photo + "&" + System.currentTimeMillis();
			switch (i) {
			case 0:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt1.setText(info.name);
					raffleBoxLayout1.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic1);
					}
				}

				break;
				
			case 1:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt2.setText(info.name);
					raffleBoxLayout2.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic2);
					}
				}
				break;
			case 2:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt3.setText(info.name);
					raffleBoxLayout3.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic3);
					}
				}
				break;
			case 3:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt4.setText(info.name);
					raffleBoxLayout4.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic4);
					}
				}
				break;
			case 4:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt5.setText(info.name);
					raffleBoxLayout5.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic5);
					}
				}
				break;
			case 5:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt6.setText(info.name);
					raffleBoxLayout6.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic6);
					}
				}

				break;
			case 6:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt7.setText(info.name);
					raffleBoxLayout7.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic7);
					}
				}

				break;
			case 7:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt8.setText(info.name);
					raffleBoxLayout8.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic8);
					}
				}

				break;
			case 8:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt9.setText(info.name);
					raffleBoxLayout9.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic9);
					}
				}

				break;
			case 9:
				if (!TextUtils.isEmpty(info.name)) {
					raffleTxt10.setText(info.name);
					raffleBoxLayout10.setBackgroundResource(R.drawable.raffle_box_on);
					if (!TextUtils.isEmpty(info.photo)) {
						ImageOperater.getInstance(context).onLoadImage(
								url, rafflePic10);
					}
				}

				break;

			default:
				break;
			}
		}
	}

	private boolean checkRaffleInput() {
		if (TextUtils.isEmpty(rafflePhoneEdit.getText().toString().trim())) {
			showToast(getString(R.string.raffle_phone_note));
			return false;
		}
		if (TextUtils.isEmpty(raffleAddressEdit.getText().toString().trim())) {
			showToast(getString(R.string.raffle_address_note));
			return false;
		}
		return true;
	}

	private void showLotteryResult() {
		if (lottery.isorder.equals("1")) {
			findViewById(R.id.raffle_btn_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.get_prize_info_layout)
					.setVisibility(View.VISIBLE);
			showWinFragment();
			isGetLottery = true;
		} else if (lottery.isorder.equals("2")) {
			showToast(getString(R.string.thank_for_join));
			raffleDo.setBackgroundResource(R.drawable.raffle_loose);
			isRaffleLoose = true;
		}
	}

	private void showWinFragment() {
		Bundle b = new Bundle();
		if (lottery != null) {
			b.putSerializable("prize_info", lottery);
		}
		displayFragment(true, "raffle_win", b, new BaseFragmentListener() {

			@Override
			public void onCallBack(Object object) {

			}
		});
	}

	private void lotteryGo() {
		final PrizeLotteryOperater prizeLottery = new PrizeLotteryOperater(
				context);
		prizeLottery.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCallBack(Object data) {
				lottery = (LotteryInfo) prizeLottery.getData();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (lottery.type.equals("1")) {
							showWinFragment();
						} else {
							showLotteryResult();
						}
					}
				});

			}
		});
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
								lotteryGo();
								dialog.dismiss();
							}
						});
				b.show();
			}
		});
	}

	private void requestPrize() {
		final PrizeListOperater prizeOp = new PrizeListOperater(context);
		prizeOp.onRequest(new RequestListener() {

			@Override
			public void onError(Object error) {
				showToast(getString(R.string.load_prize_failed));
			}

			@Override
			public void onCallBack(Object data) {
				raffInfo = (RaffleInfo) prizeOp.getData();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadPrizeInfo();
					}
				});
			}
		});
	}

	public void displayFragment(boolean isOpen, String tag, Bundle bundle,
			BaseFragmentListener listener) {
		if (isOpen) {
			showFragment(tag, -1, createFragment(tag, bundle, listener));
		} else {
			closeFragment(tag);
		}
	}

	public DialogFragment createFragment(final String tag, Bundle b,
			BaseFragmentListener listener) {
		if (tag.equals("raffle_win")) {
			RaffleWinFragment raflleF = new RaffleWinFragment(context, b);
			return raflleF;
		}
		return null;
	}

	@Override
	public void onBackPressed() {
		if (isGetLottery) {
			showLotteryTip(getString(R.string.never_get_note).replaceAll("%", lottery.name));
		} else {
			super.onBackPressed();
		}
	}

	private void showLotteryTip(final String msg) {
		Builder b = new Builder(context);
		b.setMessage(msg);
		b.setNegativeButton(getString(R.string.action_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.raffle_do:
			if (!isRaffleLoose && !isGetLottery) {
				showTipNoteDialog(getString(R.string.raffle_do_note).replaceAll("%", raffInfo.score));
			} else if (isGetLottery) {
				showToast(getString(R.string.please_done_raffle));
			} else if (isRaffleLoose && !isGetLottery) {
				isRaffleLoose = false;
				raffleDo.setBackgroundResource(R.drawable.raffle_btn_selector);
			}
			break;

		case R.id.get_raffle:
			if (checkRaffleInput()) {
				GetRaffleOperater getRO = new GetRaffleOperater(context);
				GetPrize prize = new GetPrize();
				prize.receiveKey = lottery.receiveKey;
				prize.mobile = rafflePhoneEdit.getText().toString().trim();
				prize.address = raffleAddressEdit.getText().toString().trim();
				getRO.setParams(prize);
				getRO.onRequest(new RequestListener() {

					@Override
					public void onError(Object error) {

					}

					@Override
					public void onCallBack(Object data) {
						showToast(getString(R.string.get_prize_success_note));
						isGetLottery = false;
						findViewById(R.id.raffle_btn_layout).setVisibility(
								View.GONE);
						findViewById(R.id.get_prize_info_layout).setVisibility(
								View.GONE);
					}
				});
			}
			break;

		case R.id.giveup_raffle:
			findViewById(R.id.raffle_btn_layout).setVisibility(View.GONE);
			findViewById(R.id.get_prize_info_layout).setVisibility(View.GONE);
			isGetLottery = false;
			break;
		default:
			break;
		}

	}
}
