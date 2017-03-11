package org.yancey.myhouse.collection.crawler.url.discovery

import groovy.json.JsonSlurper

import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Page

/**
 * 
 * @author yangsai
 *
 */
@groovy.util.logging.Log4j
public class DailySalesDataUrlDiscovery implements UrlDiscovery {
	
	@Override
	List<String> discoverUrl(Page page) {
		if('验证异常流量-链家网' == page.getHtml().xpath('//title/text()').get()) {
			log.error("验证异常流量，网页：${page.url}")
			throw new RuntimeException("验证异常流量，网页：${page.url}")
		}
		
		List<String> lstUrl = []
		List<String> lstAreaUrl = getAreaUrl(page)
		log.info("发现可抓取数据的销售区域: ${lstAreaUrl.size}个。")
		if(lstAreaUrl) {
			lstUrl.addAll(lstAreaUrl)
		}
		List<String> lstStreetUrl = getStreetUrl(page)
		log.info("发现可抓取的销售数据页面: ${lstStreetUrl.size}个。")
		if(!lstStreetUrl.isEmpty()) {
			lstUrl.addAll(lstStreetUrl)
		}
		return lstUrl
		
	}
	
	/**
	 * 获取地区URL，用于发现街道URL
	 * @param page
	 * @return
	 */
	List<String> getAreaUrl(Page page) {
		return page.getHtml().xpath('//div[@data-role="ershoufang"]/div[1]/a/@href').all()
	}
	
	/**
	 * 获取街道URL，用于抓取销售数据
	 * @param page
	 * @return
	 */
	List<String> getStreetUrl(Page page) {
		List<String> lstStreetUrl = []
		List<String> url = page.getHtml().xpath('//div[@data-role="ershoufang"]/div[2]/a/@href').all()
		url.each { 
			if(!DBUtil.isExist(new DailySalesData(crawlerDate:new Date().clearTime(), url:it), 'crawlerDate', 'url')) {
				lstStreetUrl << it
			}
		}
		return lstStreetUrl
	} 
	
}