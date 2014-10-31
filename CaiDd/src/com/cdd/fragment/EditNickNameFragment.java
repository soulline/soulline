package com.cdd.fragment;

import com.cdd.R;
import com.cdd.base.BaseActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class EditNickNameFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private EditText nicknameInput;
	
	private TextView editTitle;
	
	private BaseFragmentListener listener;
	
	private String nickName = "";
	
	private String inputType = "";
	
	public EditNickNameFragment() {
		super();
	}
	
	public EditNickNameFragment(Context context, Bundle b) {
		if (b != null) {
			nickName = b.getString("nick_name");
			inputType = b.getString("input_type");
		}
	}

	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.nick_name_edit_fragment, null);
		initView();
		return view;
	}
	
	private void initView() {
		view.findViewById(R.id.edit_bg_layout).setOnClickListener(this);
		nicknameInput = (EditText) view.findViewById(R.id.nickname_input);
		editTitle = (TextView) view.findViewById(R.id.edit_title);
		view.findViewById(R.id.remove_tx).setOnClickListener(this);
		view.findViewById(R.id.nick_cancel).setOnClickListener(this);
		view.findViewById(R.id.nick_ok).setOnClickListener(this);
		if (!TextUtils.isEmpty(nickName)) {
			nicknameInput.setText(nickName);
		}
		if (inputType.equals("1")) {
			editTitle.setText("编辑昵称");
			nicknameInput.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(6)});
		} else if (inputType.equals("2")) {
			editTitle.setText("编辑简介");
			nicknameInput.setFilters(new  InputFilter[]{ new  InputFilter.LengthFilter(70)});
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_bg_layout:
			dismissAllowingStateLoss();
			break;
			
		case R.id.remove_tx:
			nicknameInput.setText("");
			break;
			
		case R.id.nick_cancel:
			dismissAllowingStateLoss();
			break;
			
		case R.id.nick_ok:
			if (listener != null && !TextUtils.isEmpty(nicknameInput.getText().toString().trim())) {
				String nickName = nicknameInput.getText().toString().trim();
				listener.onCallBack(nickName);
				dismissAllowingStateLoss();
			} else if (listener != null) {
				if (getActivity() instanceof BaseActivity) {
					if (inputType.equals("1")) {
						((BaseActivity) getActivity()).showToast("请输入昵称");
					} else if (inputType.equals("2")) {
						((BaseActivity) getActivity()).showToast("请输入简介");
					}
				}
			}
			break;

		default:
			break;
		}
		
	}
	
	
}
