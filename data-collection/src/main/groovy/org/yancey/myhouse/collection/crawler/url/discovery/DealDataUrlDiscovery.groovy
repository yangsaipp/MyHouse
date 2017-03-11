package org.yancey.myhouse.collection.crawler.url.discovery

import java.util.List;

import groovy.json.JsonSlurper
import groovy.transform.Canonical

import org.codehaus.groovy.transform.trait.Traits.Implemented;
import org.yancey.myhouse.collection.model.DealData;
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 
 * @author yangsai
 *
 */
@groovy.util.logging.Log4j
abstract class DealDataUrlDiscovery implements UrlDiscovery {
	
	@Override
	List<String> discoverUrl(Page page) {
		if('验证异常流量-链家网' == page.getHtml().xpath('//title/text()').get()) {
			log.error("验证异常流量，网页：${page.url}")
			throw new RuntimeException("验证异常流量，网页：${page.url}")
		}
		
		List<String> lstUrl = []
		String nextPageUrl = getNextPageUrl(page)
		if(nextPageUrl) {
			lstUrl << nextPageUrl
		}
		List<String> lstDetailUrl = getDetailUrl(page)
		log.info("发现可抓取的成交数据详情页面: ${lstDetailUrl.size}个。")
		if(!lstDetailUrl.isEmpty()) {
			lstUrl.addAll(lstDetailUrl)
		}
		return lstUrl
		
	}
	
	
	Object getPageNoInfo(Page page) {
		// 获取页面翻页信息
		String jsonString = page.getHtml().xpath("//div[@class=\"leftContent\"]//div[@class='page-box house-lst-page-box']/@page-data").toString()
		return new JsonSlurper().parseText(jsonString)
	}
	
	abstract String getNextPageUrl(Page page)
	
	abstract List<String> getDetailUrl(Page page) 
	
}