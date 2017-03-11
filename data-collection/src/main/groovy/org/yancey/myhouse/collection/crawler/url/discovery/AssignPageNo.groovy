package org.yancey.myhouse.collection.crawler.url.discovery

import groovy.json.JsonSlurper

import org.yancey.myhouse.collection.crawler.CrawlContext;
import org.yancey.myhouse.collection.model.DealData
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Page

/**
 * 
 * 根据指定最近页面数来抓取的成交信息，一般用于初始化抓取完后每日抓取使用
 * @author yangsai
 *
 */
@groovy.util.logging.Log4j
class AssignPageNo extends DealDataUrlDiscovery {
	
	static final String deal_max_page_no = 'DEAL_MAX_PAGE_NO' 
	
	
	String getNextPageUrl(Page page) {
		
		
		// 获取页面翻页信息
		String jsonString = page.getHtml().xpath("//div[@class=\"leftContent\"]//div[@class='page-box house-lst-page-box']/@page-data").toString()
		def pageNoInfo = new JsonSlurper().parseText(jsonString)
		
		if(isOverstep(pageNoInfo)) {
			return null;
		}
		
		int curPage = pageNoInfo.curPage
		int totalPage = pageNoInfo.totalPage
		if(curPage == totalPage){		// 最后一页
			return null;
		}
		
		List<String> pageUrl = page.getUrl().toString().split('/') as List
		if(pageUrl[-1].startsWith('pg')) {
			pageUrl[-1] = "pg${curPage + 1}"
		}else {
			pageUrl << "pg${curPage + 1}"
		}
		return (pageUrl.join('/') + '/')
	}
	
	List<String> getDetailUrl(Page page) {
		List<String> lstDetailUrl = []
		List<String> detailUrl = page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li/a[@class='img']/@href").all()
		detailUrl.each {url->
			if(!DBUtil.isExist(new DealData(url: url), 'url')) {
				lstDetailUrl << url
			}
		}
		return lstDetailUrl
	}
	
	boolean isOverstep(pageNoInfo) {
		Integer maxPageNo;
		
		if(CrawlContext.contains(deal_max_page_no)) {
			maxPageNo = CrawlContext.get(deal_max_page_no)
		} else {
			maxPageNo = pageNoInfo.curPage + Condition.dealPageNo
			log.info("确定抓取成交列表页面最大翻页值：$maxPageNo")
			CrawlContext.put(deal_max_page_no, maxPageNo)
		}
		
		
		return pageNoInfo.curPage >= maxPageNo
	}
	
}