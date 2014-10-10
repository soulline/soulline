package com.example.testbutton;

import android.app.Activity;  
import android.content.Context;  
import android.graphics.drawable.ColorDrawable;  
import android.view.LayoutInflater;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.View.OnTouchListener;  
import android.view.ViewGroup.LayoutParams;  
import android.widget.Button;  
import android.widget.PopupWindow;  
import android.widget.TextView;
   
public class ParaSettingPopupWindow extends PopupWindow {  
   
   
	public TextView lujing,lianjie,tuichu;
	
    private View mMenuView;  
    private Context mContext;
    
    public View getView() {
		return mMenuView;
	}
   
    public ParaSettingPopupWindow(Context context,OnClickListener itemsOnClick) {  
        super(context);  
//        LayoutInflater inflater = (LayoutInflater) context  
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        
        LayoutInflater inflater = LayoutInflater.from(context);
        
        mMenuView = inflater.inflate(R.layout.parasetting, null);  
//        lujing = (TextView)mMenuView.findViewById(R.id.lujing);
//		lianjie = (TextView)mMenuView.findViewById(R.id.lianjie);
//		tuichu = (TextView)mMenuView.findViewById(R.id.tuichu);
       
		
//		lujing.setOnClickListener(itemsOnClick);  
//		lianjie.setOnClickListener(itemsOnClick);  
//		tuichu.setOnClickListener(itemsOnClick);  
        
        this.setContentView(mMenuView);  
        this.setWidth(1200);  
        this.setHeight(700);  
        this.setFocusable(true);  
//        this.setAnimationStyle(R.style.AnimBottom);  
//        ColorDrawable dw = new ColorDrawable(0xb0000000);  
 //        this.setBackgroundDrawable(dw);  
  //        mMenuView.setOnTouchListener(new OnTouchListener() {  
//               
//            public boolean onTouch(View v, MotionEvent event) {  
//                   
//                int height = mMenuView.findViewById(R.id.file_pop).getTop();  
//                int y=(int) event.getY();  
//                if(event.getAction()==MotionEvent.ACTION_UP){  
//                    if(y<height){  
//                        dismiss();  
//                    }  
//                }                 
//                return true;  
//            }  
//        });  
        
   
    }  
   
}  