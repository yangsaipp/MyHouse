package org.yancey.myhouse.collection.crawler.helper

import groovy.transform.Canonical

import org.yancey.myhouse.collection.crawler.url.discovery.DailySalesDataUrlDiscovery
import org.yancey.myhouse.collection.crawler.url.discovery.UrlDiscovery

import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 主要用于发现和过滤每日销售数页面URL
 * 
 * @author yangsai
 * @see org.yancey.myhouse.collection.crawler.url.discovery.AssignPageNo
 */
@Canonical
@TargetUrl(value = ['http://*.lianjia.com/ershoufang/\\w+/'], sourceRegion = '/none')
@groovy.util.logging.Log4j
class DailySalesDataHelper extends DataHelper {
	
	UrlDiscovery urlDiscovery = new DailySalesDataUrlDiscovery()
	
}