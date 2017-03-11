package org.yancey.myhouse.collection.crawler.url.discovery

import groovy.json.JsonSlurper
import groovy.transform.Canonical

import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 用于发现待抓取数据的页面地址
 * @author yangsai
 *
 */
public interface UrlDiscovery {
	
	List<String> discoverUrl(Page page);
}