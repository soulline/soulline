package com.cdd.activity.findpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.activity.findpage.DynamicAdapter.OnAnswerMemberClickLister;
import com.cdd.mode.DynamicEntry;
import com.cdd.mode.DynamicReplay;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReplyDetaiAdapter extends ArrayAdapter<DynamicReplay> {

	private Context context;
	
	public interface OnAnswerMemberClickLister {
		public void onAnswerClick(DynamicReplay replay, int position);
	}
	
	private OnAnswerMemberClickLister answerClickListener;
	
	public ReplyDetaiAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	public void addData(ArrayList<DynamicReplay> list) {
		synchronized (list) {
			for (DynamicReplay reply : list) {
				add(reply);
			}
		}
	}
	
	public void addOnAnswerMemberClickLister(OnAnswerMemberClickLister answerClickListener) {
		this.answerClickListener = answerClickListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.newsdetail_reply_item, null);
			holder.answerTx = (TextView) convertView.findViewById(R.id.answer_tx);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SpannableString spanStr = getSpannableString(getItem(position), position);
		holder.answerTx.setHighlightColor(Color.TRANSPARENT);
		holder.answerTx.setText(spanStr);
		holder.answerTx.setMovementMethod(LinkMovementMethod.getInstance());
		if (position == (getCount() - 1)) {
			convertView.findViewById(R.id.bottom_dash_line).setVisibility(
					View.GONE);
		} else {
			convertView.findViewById(R.id.bottom_dash_line).setVisibility(
					View.VISIBLE);
		}
		return convertView;
	}
	
	private SpannableString getSpannableString(final DynamicReplay replay,
			final int position) {
		String span = replay.memberName + "：" + replay.message;
		SpannableString spanStr = new SpannableString(span);
		spanStr.setSpan(new ClickableSpan() {

			@Override
			public void updateDrawState(TextPaint ds) {
				super.updateDrawState(ds);
				ds.setColor(Color.YELLOW);
				ds.setUnderlineText(false);
			}

			@Override
			public void onClick(View widget) {
				if (answerClickListener != null) {
					answerClickListener.onAnswerClick(replay, position);
				}
			}
		}, 0, (replay.memberName.length() + 1),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanStr.setSpan(new ForegroundColorSpan(context.getResources()
				.getColor(R.color.messageto_tx_selector)), 0, (replay.memberName
				.length() + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanStr;
	}

	public class ViewHolder {
		public TextView answerTx;
	}
	
}
