package com.yyl.mode;

import java.util.ArrayList;

public class RoomListInfo extends YylEntry {

	public int pageNum = 0;
	
	public int pageSize = 0;
	
	public int totalCount = 0;
	
	public int totalPage = 0;
	
	public ArrayList<RoomItemEntry> roomList = new ArrayList<RoomItemEntry>();
}
