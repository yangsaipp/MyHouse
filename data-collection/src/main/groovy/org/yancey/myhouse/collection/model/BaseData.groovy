package org.yancey.myhouse.collection.model;

import java.sql.Timestamp

import us.codecraft.webmagic.Page


class BaseData {

	/* id **/
	String id
	
	/* 创建时间 **/
	Timestamp createTime
	
	public void afterCrawler(Page page) {
		if('验证异常流量-链家网' == page.getHtml().xpath('//title/text()').get()) {
			throw new RuntimeException("验证异常流量，网页：${page.url}")
		}
		id = UUID.randomUUID().toString(); 
		createTime = new Timestamp(new Date().getTime())
	}
}
