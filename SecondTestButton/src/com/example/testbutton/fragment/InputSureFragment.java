package com.example.testbutton.fragment;

import com.example.testbutton.R;
import com.example.testbutton.mode.InputEntry;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputSureFragment extends DialogFragment implements OnClickListener{

	private View view;
	
	private Context context;
	
	private BaseFragmentListener listener;
	
	private EditText inputContent;
	
	private InputEntry inputEntry = new InputEntry();
	
	private TextView inputTitle;
	
	public InputSureFragment(Context context, Bundle b) {
		this.context = context;
		if (b != null) {
			inputEntry.type = b.getInt("type", 0);
		}
	}
	
	public void addFragmentListener(BaseFragmentListener listener) {
		this.listener = listener;
	}
	
	private void initTitle(int type) {
		switch (type) {
		case 1:
			inputTitle.setText("仓号设定");
			break;
		case 2:
			inputTitle.setText("水分设定");
			break;
		case 3:
			inputTitle.setText("数量设定");
			break;
		case 4:
			inputTitle.setText("检测时间设定");
			break;
		case 5:
			inputTitle.setText("排空时间设定");
			break;
			
		case 6:
			inputTitle.setText("间隔时间");
			break;

		default:
			break;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.input_sure_fragment, null);
		initView();
		initTitle(inputEntry.type);
		return view;
	}
	
	private void initView() {
		inputContent = (EditText) view.findViewById(R.id.input_content);
		inputTitle = (TextView) view.findViewById(R.id.input_title);
		view.findViewById(R.id.content_ok).setOnClickListener(this);
		view.findViewById(R.id.content_cancel).setOnClickListener(this);
	}
	
	private boolean checkInput() {
		if (TextUtils.isEmpty(inputContent.getText().toString().trim())) {
			Toast.makeText(context, "请输入参数值再确定", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	@Override 
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.content_ok:
			if (checkInput()) {
				inputEntry.value = inputContent.getText().toString().trim();
				if (listener != null) {
					listener.onCallBack(inputEntry);
					dismissAllowingStateLoss();
				}
			}
			break;
		case R.id.content_cancel:
			dismissAllowingStateLoss();
			break;
		default:
			break;
		}
		
	}

}
