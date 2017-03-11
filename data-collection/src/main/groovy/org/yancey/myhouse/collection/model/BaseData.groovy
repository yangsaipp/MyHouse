package org.yancey.myhouse.collection.model;

import java.sql.Timestamp

import us.codecraft.webmagic.Page

@groovy.util.logging.Log4j
class BaseData {

	/* id **/
	String id
	
	/* 创建时间 **/
	Timestamp createTime
	
	/** 页面地址 */
	String url
	
	public void afterCrawler(Page page) {
		if('验证异常流量-链家网' == page.getHtml().xpath('//title/text()').get()) {
			log.error("验证异常流量，网页：${page.url}")
			throw new RuntimeException("验证异常流量，网页：${page.url}")
		}
		id = UUID.randomUUID().toString(); 
		createTime = new Timestamp(new Date().getTime())
		url = page.getUrl()
	}
}
