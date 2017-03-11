package org.yancey.myhouse.collection.crawler.url.discovery

import us.codecraft.webmagic.Page

/**
 * 根据指定的交易日期来抓取成交数据时，用于第一次初始化抓取时使用
 * @author yangsai
 *
 */
class AssignDate extends DealDataUrlDiscovery {
	
	String getNextPageUrl(Page page) {
		
		if(isOverTime(page)) {	// 页面成交时间比指定时间早，不翻页，停止抓取数据
			return;
		}
		
		// 获取页面翻页信息
		def pageNoInfo = getPageNoInfo(page)
		int curPage = pageNoInfo.curPage
		int totalPage = pageNoInfo.totalPage
		if(curPage == totalPage){		// 最后一页
			return;
		}
		
		List<String> pageUrl = page.getUrl().toString().split('/') as List
		if(pageUrl[-1].startsWith('pg')) {
			pageUrl[-1] = "pg${curPage + 1}"
		}else {
			pageUrl << "pg${curPage + 1}"
		}
		return (pageUrl.join('/') + '/');
	}
	
	List<String> getDetailUrl(Page page) {
		List<String> lstDetailUrl = []
		List<String> detailUrl = page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li/a[@class='img']/@href").all()
		// 符合条件的交易记录才抓取
		List<String> dealDateList = page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li//div[@class='dealDate']/text()").all();
		int index = 0
		dealDateList.each {
			if(!isOverTime(it)) {
				lstDetailUrl << (detailUrl[index]);
			}
			index ++
		}
		return lstDetailUrl
	}
	
	/**
	 * 成交日期是否超出指定的时间
	 * @param page
	 * @return	true 是   false 否
	 */
	boolean isOverTime(Page page) {
		// 获取列表页面所有的交易时间
		List<String> dealDateList = page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li//div[@class='dealDate']/text()").all();
		return isOverTime(dealDateList[-1])
	}
	
	/**
	 * 成交日期是否超出指定的时间
	 * @param pageDealDate 如 2017.03.10
	 * @return	true 是   false 否
	 */
	boolean isOverTime(String pageDealDate) {
		return Condition.dealDate.compareTo(Date.parse("yyyy.MM.dd", pageDealDate)) == 1
	}
	
}