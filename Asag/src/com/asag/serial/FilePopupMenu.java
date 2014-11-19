package com.asag.serial;

import com.asag.serial.utils.DataUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class FilePopupMenu extends PopupWindow implements OnClickListener{

	private Context context;

	private View view;
	
	private OnFileClickListener listener;
	
	private TextView routeMenu, connectOrShutMenu, exitMenu;
	
	public interface OnFileClickListener {
		public void onClick(int resourceId);
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		switch (size) {
		case 1:
			reloadNewTextSize(1.0f);
			this.setWidth(80 * 1);
			break;
		case 2:
			reloadNewTextSize(1.2f);
			this.setWidth((80 * 6) / 5);
			break;
		case 3:
			reloadNewTextSize(1.4f);
			this.setWidth((80 * 7) / 5);
			break;
		case 4:
			reloadNewTextSize(1.6f);
			this.setWidth((80 * 8) / 5);
			break;
		case 5:
			reloadNewTextSize(1.8f);
			this.setWidth((80 * 9) / 5);
			break;

		default:
			break;
		}
	}
	
	private void reloadNewTextSize(float size) {
		routeMenu.setTextSize(routeMenu.getTextSize() * size);
		connectOrShutMenu.setTextSize(connectOrShutMenu.getTextSize() * size);
		exitMenu.setTextSize(exitMenu.getTextSize() * size);
	}

	public FilePopupMenu(Context context, OnFileClickListener listener) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.file_pop_menu, null);
		this.listener = listener;
		this.setContentView(view);
		
		
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
        initTextSize();
	}
	
	private void initView() {
		routeMenu = (TextView) view.findViewById(R.id.route_menu);
		connectOrShutMenu = (TextView) view.findViewById(R.id.connect_or_shut_menu);
		exitMenu = (TextView) view.findViewById(R.id.exit_menu);
		view.findViewById(R.id.route_menu).setOnClickListener(this);
		view.findViewById(R.id.connect_or_shut_menu).setOnClickListener(this);
		view.findViewById(R.id.exit_menu).setOnClickListener(this);
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
