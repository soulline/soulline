package com.cdd.fragment;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.net.RequestListener;
import com.cdd.operater.DynamicReplyOp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class BottomReplyFragment extends DialogFragment implements
		OnClickListener {

	private View view;

	private Context context;

	private EditText messageInput;
	
	private BaseFragmentListener listener;
	
	private String cofId;

	public BottomReplyFragment(Context context, Bundle b) {
		this.context = context;
		if (b != null) {
			cofId = b.getString("cofId");
		}
	}
	
	public void addBaseFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.bottom_reply_fragment, null);
		initView();
		initContent();
		return view;
	}

	private void initView() {
		messageInput = (EditText) view.findViewById(R.id.message_input);
		view.findViewById(R.id.reply_add_bq).setOnClickListener(this);
		view.findViewById(R.id.reply_send).setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		messageInput.setText("");
		((InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				getActivity().getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		super.onDestroyView();
	}

	private void initContent() {

	}
	
	private void sendReply() {
		if (!TextUtils.isEmpty(messageInput.getText().toString().trim())) {
			String message = messageInput.getText().toString().trim();
			if (getActivity() instanceof BaseActivity) {
				DynamicReplyOp replyOp = new DynamicReplyOp(((BaseActivity) getActivity()));
				replyOp.setParams(cofId, message);
				replyOp.onRequest(new RequestListener() {
					
					@Override
					public void onError(Object error) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onCallBack(Object data) {
						if (getActivity() instanceof BaseActivity) {
							((BaseActivity) getActivity()).showToast("回复成功");
						}
						if (listener != null) {
							listener.onCallBack("success");
						}
						dismissAllowingStateLoss();
					}
				});
			}
		} else {
			if (getActivity() instanceof BaseActivity) {
				((BaseActivity) getActivity()).showToast("请输入回复内容");
			}
			return;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reply_add_bq:

			break;

		case R.id.reply_send:
			sendReply();
			break;

		default:
			break;
		}

	}

}
