package com.asag.serial;

import java.util.ArrayList;

import com.asag.serial.LiangzhongPopMenu.OnLiangClickListener;
import com.asag.serial.mode.SpinnerItem;
import com.asag.serial.utils.DataUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class ChandiPopMenu extends PopupWindow {



	private Context context;

	private View view;
	
	private OnChandiClickListener listener;
	
	private ListView spinnerList;
	
	private SerialSpinnerAdapter adapter;
	
	public interface OnChandiClickListener {
		public void onClick(SpinnerItem item);
	}
	
	private void initTextSize() {
		int size = DataUtils.getPreferences(DataUtils.KEY_TEXT_SIZE, 1);
		if (adapter != null) {
			adapter.setItemTextSize(size);
			adapter.notifyDataSetChanged();
		}
	}

	public ChandiPopMenu(Context context, OnChandiClickListener listener, ArrayList<SpinnerItem> list) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.spinner_pop_menu, null);
		this.listener = listener;
		this.setContentView(view);
		this.setWidth(50);
		this.setHeight(200);
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
            this.showAsDropDown(parent, parent.getLayoutParams().width / 8, 0);  
        } else {  
            this.dismiss();  
        }  
    }


}
