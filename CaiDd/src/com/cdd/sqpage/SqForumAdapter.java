package com.cdd.sqpage;

import java.util.ArrayList;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.util.ImageOperater;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SqForumAdapter extends ArrayAdapter<ForumEntry> {

	private Context context;
	
	public SqForumAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	public void addData(ArrayList<ForumEntry> list) {
		synchronized (list) {
			for (ForumEntry entry : list) {
				add(entry);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.forum_list_item, null);
			holder.forumTitle = (TextView) convertView.findViewById(R.id.forum_title);
			holder.zanCount = (TextView) convertView.findViewById(R.id.zan_count);
			holder.answer1Content = (TextView) convertView.findViewById(R.id.answer1_content);
			holder.answer1Icon = (ImageView) convertView.findViewById(R.id.answer1_icon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ForumEntry entry = getItem(position);
		holder.forumTitle.setText(entry.forumTitle);
		holder.zanCount.setText("（"+ entry.zanCount + "）");
		holder.answer1Content.setText(entry.answerContent);
		if (!TextUtils.isEmpty(entry.answerPic)) {
			ImageOperater.getInstance(context).onLoadImage(entry.answerPic, holder.answer1Icon);
		}
		convertView.findViewById(R.id.zan_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((BaseActivity) context).showToast("赞赞赞赞赞");
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		public TextView forumTitle, zanCount, answer1Content;
		
		public ImageView answer1Icon;
	}

}
