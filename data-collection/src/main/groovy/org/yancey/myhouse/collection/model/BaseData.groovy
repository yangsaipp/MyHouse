package org.yancey.myhouse.collection.model;

import java.sql.Timestamp


class BaseData {

	/* id **/
	String id
	
	/* 创建时间 **/
	Timestamp createTime
	
	public void afterCrawler() {
		id = UUID.randomUUID().toString(); 
		createTime = new Timestamp(new Date().getTime())
	}
}
