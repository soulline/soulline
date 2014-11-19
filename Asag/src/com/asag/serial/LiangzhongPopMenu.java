package com.asag.serial;

import java.util.ArrayList;

import com.asag.serial.mode.SpinnerItem;
import com.asag.serial.utils.DataUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class LiangzhongPopMenu extends PopupWindow {


	private Context context;

	private View view;
	
	private OnLiangClickListener listener;
	
	private ListView spinnerList;
	
	private SerialSpinnerAdapter adapter;
	
	public interface OnLiangClickListener {
		public void onClick(SpinnerItem item);
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		if (adapter != null) {
			adapter.setItemTextSize(size);
			adapter.notifyDataSetChanged();
		}
		switch (size) {
		case 1:
			this.setWidth(60 * 1);
			break;
		case 2:
			this.setWidth((60 * 6) / 5);
			break;
		case 3:
			this.setWidth((60 * 7) / 5);
			break;
		case 4:
			this.setWidth((60 * 8) / 5);
			break;
		case 5:
			this.setWidth((60 * 9) / 5);
			break;

		default:
			break;
		}
	}

	public LiangzhongPopMenu(Context context, OnLiangClickListener listener, ArrayList<SpinnerItem> list) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.spinner_pop_menu, null);
		this.listener = listener;
		this.setContentView(view);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
        this.update();  
        ColorDrawable dw = new ColorDrawable(0000000000);  
        this.setBackgroundDrawable(dw);  
        this.setAnimationStyle(R.style.AnimationPreview);
        initView();
        adapter = new SerialSpinnerAdapter(context);
        adapter.addData(list);
        spinnerList.setAdapter(adapter);
        initTextSize();
	}
	
	private void initView() {
		spinnerList = (ListView) view.findViewById(R.id.spinner_list);
		spinnerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SpinnerItem item = adapter.getItem(position);
				if (listener != null) {
					listener.onClick(item);
					dismiss();
				}
			}
		});
	}
	
	public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);  
        } else {  
            this.dismiss();  
        }  
    }

}
