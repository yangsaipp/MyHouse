package org.yancey.myhouse.collection.crawler.helper

import org.yancey.myhouse.collection.crawler.url.discovery.UrlDiscovery

import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor

/**
 * 用于帮助发现要抓取数据的页面地址，继承Webmagic的类AfterExtractor
 * 
 * @author yangsai
 */
@groovy.util.logging.Log4j
abstract class DataHelper implements AfterExtractor {
	protected UrlDiscovery urlDiscovery
	@Override
	public void afterProcess(Page page) {
		List<String> lstUrl = this.getUrlDiscovery().discoverUrl(page)
		int addUrlNum = 0
		if(lstUrl) {
			page.addTargetRequests(lstUrl)
			addUrlNum = lstUrl.size()
		}
		log.info("添加URL到Webmagic，添加URL个数：$addUrlNum")
		page.setSkip(true)
	}
	
	abstract UrlDiscovery getUrlDiscovery()
}