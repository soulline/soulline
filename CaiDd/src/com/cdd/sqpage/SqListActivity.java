package com.cdd.sqpage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdd.R;
import com.cdd.base.BaseActivity;
import com.cdd.mode.ForumEntry;

public class SqListActivity extends BaseActivity implements OnClickListener{

	private ListView sqContentList;
	
	private SqForumAdapter sqAdapter;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.sq_list_activity);
		initView();
		initContent();
	}

	private void initView() {
		findViewById(R.id.search_icon).setOnClickListener(this);
		findViewById(R.id.sq_ask_layout).setOnClickListener(this);
		sqContentList = (ListView) findViewById(R.id.sq_content_list);
		sqContentList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	private void initContent() {
		showSqListView();
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
			sqAdapter = new SqForumAdapter(context);
			sqAdapter.addData(list);
			sqContentList.setAdapter(sqAdapter);
		} else {
			sqAdapter.clear();
			sqAdapter.addData(list);
			sqAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_icon:
			
			break;

		case R.id.sq_ask_layout:
			Intent askIntent = new Intent(context, PulishActivity.class);
			startActivity(askIntent);
			break;
		default:
			break;
		}
		
	}

}
