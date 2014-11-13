package com.cdd.mode;

import java.util.ArrayList;

public class DynamicEntry extends BaseEntry {

	public String content = "";
	
	public String createTime = "";
	
	public String favoriteCount = "";
	
	public String id = "";
	
	public String isHot = "";
	
	public String likeCount = "";
	
	public String memberId = "";
	
	public String memberLevelName = "";
	
	public String memberName = "";
	
	public String memberPhoto = "";
	
	public String memberSex = "";
	
	public String shareCount = "";
	
	public String status = "";
	
	public String type = "";
	
	public boolean isPack = true;
	
	public ArrayList<PhotosEntry> photos = new ArrayList<PhotosEntry>();
	
	public ArrayList<DynamicReplay> replyList = new ArrayList<DynamicReplay>();
	
	public ForwardNewsEntry forward = new ForwardNewsEntry();
	
	public String isForward = "";
	
}
