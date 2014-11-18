package com.asag.serial;

import com.asag.serial.SettingPopMenu.OnSettingClickListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class TextSizeMenu extends PopupWindow implements OnClickListener{

	private Context context;

	private View view;
	
	private OnTextSizeClickListener listener;
	
	public interface OnTextSizeClickListener {
		public void onClick(int resourceId);
	}

	public TextSizeMenu(Context context, OnTextSizeClickListener listener) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.textsize_set_menu, null);
		this.listener = listener;
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(100);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态  
        this.update();  
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        this.setBackgroundDrawable(dw);  
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        // 设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimationPreview);
        initView();
	}
	
	private void initView() {
		view.findViewById(R.id.s_small_size).setOnClickListener(this);
		view.findViewById(R.id.small_size).setOnClickListener(this);
		view.findViewById(R.id.normal_size).setOnClickListener(this);
		view.findViewById(R.id.big_size).setOnClickListener(this);
		view.findViewById(R.id.b_big_size).setOnClickListener(this);
	}
	
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 5);  
        } else {  
            this.dismiss();  
        }  
    }

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onClick(v.getId());
		}
		this.dismiss();
	}


}
