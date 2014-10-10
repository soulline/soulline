package com.yyl.game;

import java.util.ArrayList;

import com.yyl.R;
import com.yyl.mode.RoomItemEntry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RoomListAdapter extends ArrayAdapter<RoomItemEntry> {

	private Context context;
	public RoomListAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}
	
	synchronized public void addAll(ArrayList<RoomItemEntry> list) {
		synchronized (list) {
			for (RoomItemEntry gameItem : list) {
				add(gameItem);
			}
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.game_list_item, null);
			holder = new ViewHolder();
			holder.gameId = (TextView) convertView.findViewById(R.id.room_id);
			holder.gameScore = (TextView) convertView.findViewById(R.id.base_coin);
			holder.people1 = (ImageView) convertView.findViewById(R.id.people_star_1);
			holder.people2 = (ImageView) convertView.findViewById(R.id.people_star_2);
			holder.people3 = (ImageView) convertView.findViewById(R.id.people_star_3);
			holder.people4 = (ImageView) convertView.findViewById(R.id.people_star_4);
			holder.people5 = (ImageView) convertView.findViewById(R.id.people_star_5);
			holder.roomStatus = (TextView) convertView.findViewById(R.id.room_status);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		RoomItemEntry gameItem = getItem(position);
		holder.gameId.setText(gameItem.boardNo);
		holder.gameScore.setText(gameItem.score);
		if (gameItem.isStarting) {
			holder.roomStatus.setText(context.getString(R.string.game_doing));
		} else {
			holder.roomStatus.setText(context.getString(R.string.game_holding));
		}
		initGamer(gameItem.gamerNum, holder);
		return convertView;
	}
	
	private void initGamer(int gamerNum, ViewHolder holder) {
		switch (gamerNum) {
		case 1:
			holder.people1.setBackgroundResource(R.drawable.people_bright);
			holder.people2.setBackgroundResource(R.drawable.people_gray);
			holder.people3.setBackgroundResource(R.drawable.people_gray);
			holder.people4.setBackgroundResource(R.drawable.people_gray);
			holder.people5.setBackgroundResource(R.drawable.people_gray);
			break;
		case 2:
			holder.people1.setBackgroundResource(R.drawable.people_bright);
			holder.people2.setBackgroundResource(R.drawable.people_bright);
			holder.people3.setBackgroundResource(R.drawable.people_gray);
			holder.people4.setBackgroundResource(R.drawable.people_gray);
			holder.people5.setBackgroundResource(R.drawable.people_gray);
			break;
		case 3:
			holder.people1.setBackgroundResource(R.drawable.people_bright);
			holder.people2.setBackgroundResource(R.drawable.people_bright);
			holder.people3.setBackgroundResource(R.drawable.people_bright);
			holder.people4.setBackgroundResource(R.drawable.people_gray);
			holder.people5.setBackgroundResource(R.drawable.people_gray);
			break;
		case 4:
			holder.people1.setBackgroundResource(R.drawable.people_bright);
			holder.people2.setBackgroundResource(R.drawable.people_bright);
			holder.people3.setBackgroundResource(R.drawable.people_bright);
			holder.people4.setBackgroundResource(R.drawable.people_bright);
			holder.people5.setBackgroundResource(R.drawable.people_gray);
			break;
		case 5:
			holder.people1.setBackgroundResource(R.drawable.people_bright);
			holder.people2.setBackgroundResource(R.drawable.people_bright);
			holder.people3.setBackgroundResource(R.drawable.people_bright);
			holder.people4.setBackgroundResource(R.drawable.people_bright);
			holder.people5.setBackgroundResource(R.drawable.people_bright);
			break;
		default:
			break;
		}
	}

	public class ViewHolder{
		public TextView gameId, gameScore, roomStatus;
		public ImageView people1, people2, people3, people4, people5;
	}
	
}
