package com.cdd.minepage;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.cdd.R;
import com.cdd.app.CddApp;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ForumEntry;
import com.cdd.sqpage.SqForumAdapter;

public class MyForumActivity extends BaseActivity {

	private int contentType = -1;
	
	private SqForumAdapter sqAdapter;
	
	private ListView sqListview;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_forum_activity);
		contentType = getIntent().getIntExtra("content_type", -1);
		initContentTitle(contentType);
		initView();
		initContent();
	}
	
	private void initContent() {
		showSqListView();
	}
	
	private void initView() {
		sqListview = (ListView) findViewById(R.id.sq_listview);
	}
	
	private void showSqListView() {
		ArrayList<ForumEntry> list = new ArrayList<ForumEntry>();
		ForumEntry entry1 = new ForumEntry();
		entry1.forumTitle = "财会考试是几号啊？";
		entry1.zanCount = "126";
		entry1.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry1);
		ForumEntry entry2 = new ForumEntry();
		entry2.forumTitle = "财会考试是几号啊？";
		entry2.zanCount = "126";
		entry2.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry2);
		ForumEntry entry3 = new ForumEntry();
		entry3.forumTitle = "财会考试是几号啊？";
		entry3.zanCount = "126";
		entry3.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry3);
		ForumEntry entry4 = new ForumEntry();
		entry4.forumTitle = "财会考试是几号啊？";
		entry4.zanCount = "126";
		entry4.answerContent = "纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁纪委及挖掘我既入境问禁";
		list.add(entry4);
		initSqList(list);
	}
	
	private void initSqList(ArrayList<ForumEntry> list) {
		if (sqAdapter == null) {
			sqAdapter = new SqForumAdapter(CddApp.getInstance());
			sqAdapter.addData(list);
			sqListview.setAdapter(sqAdapter);
		} else {
			sqAdapter.clear();
			sqAdapter.addData(list);
			sqAdapter.notifyDataSetChanged();
		}
	}
	
	private void initContentTitle(int type) {
		switch (type) {
		case 0:
			initTitle("我的提问");
			break;
			
		case 1:
			initTitle("我的回答");
			break;
			
		case 2:
			initTitle("我的收藏");
			break;

		default:
			break;
		}
	}

}
