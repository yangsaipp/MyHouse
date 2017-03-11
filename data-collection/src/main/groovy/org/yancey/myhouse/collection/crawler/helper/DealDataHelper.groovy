package org.yancey.myhouse.collection.crawler.helper

import groovy.transform.Canonical

import org.yancey.myhouse.collection.crawler.url.discovery.AssignPageNo
import org.yancey.myhouse.collection.crawler.url.discovery.UrlDiscovery

import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 主要用于发现和过滤房屋成交详情页面URL问题，主要包括两种情况：
 * <ul>
 * 	<li>
 * 		成交详情分页URL处理
 * 	</li>
 * <li>
 * 抓取时丢弃不符合条件的成交详情数据，主要分两种情况：<br>
 * 1. 第一次初始化抓取时，抓取指定成交日期之前的数据，如只抓取2016-01-01之前成交的数据<br>
 * 2. 每日抓取时，抓取指定最近成交页面列表翻页内数据，如只抓取最近10页成交的数据。<br>
 * 	</li>
 * </ul>
 * 
 * @author yangsai
 * @see org.yancey.myhouse.collection.crawler.url.discovery.AssignDate
 * @see org.yancey.myhouse.collection.crawler.url.discovery.AssignPageNo
 */
@Canonical
@TargetUrl(value = ['http://*.lianjia.com/chengjiao/pg\\d+/','http://*.lianjia.com/chengjiao/\\w+/pg\\d+/'], sourceRegion = '/none')
@groovy.util.logging.Log4j
class DealDataHelper extends DataHelper {
	
	UrlDiscovery urlDiscovery = new AssignPageNo()
	
}