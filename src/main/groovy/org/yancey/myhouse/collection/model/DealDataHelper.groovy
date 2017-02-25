package org.yancey.myhouse.collection.model

import groovy.json.JsonSlurper
import groovy.transform.Canonical
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 主要用于发现和过滤房屋成交详情页面URL问题，主要包括两种情况：
 * <ul>
 * 	<li>
 * 		成交详情分页URL处理
 * 	</li>
 * <li>
 * 抓取时丢弃不符合条件的成交详情数据，主要分两种情况：<br>
 * 1. 第一次初始化抓取时，超出指定日期<br>
 * 2. 每日抓取时只抓取成最新的成交数据（成交日期比数据库数据新的数据）<br>
 * 	</li>
 * </ul>
 * 
 * @author yangsai
 *
 */
@Canonical
@TargetUrl('http://*.lianjia.com/chengjiao/\\w+/pg\\d/')
class DealDataHelper implements AfterExtractor {
	
	@Override
	public void afterProcess(Page page) {
		addNextPage(page)
		page.setSkip(true)
	}
	
	void addNextPage(Page page) {
		
		if(isOverTime(page)) {	// 页面成交时间比指定时间早，不翻页，停止抓取数据
			return;
		}
		
		// 获取页面翻页信息
		String jsonString = page.getHtml().xpath("//div[@class=\"leftContent\"]//div[@class='page-box house-lst-page-box']/@page-data").toString()
		def pageNoInfo = new JsonSlurper().parseText(jsonString)
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
		page.addTargetRequest(pageUrl.join('/') + '/');
	}
	
	/**
	 * 成交日期是否超出指定的时间
	 * @param page
	 * @return	true 是   false 否
	 */
	boolean isOverTime(Page page) {
		Date filterData = Date.parse("yyyy.MM.dd", "2017.01.06")
		int run_val
		// 获取列表页面所有的交易时间
		List<String> dealDateList = page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li//div[@class='dealDate']/text()").all();
//		String dealDate = page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li//div[@class='dealDate' and last()]/text()").all();
		println dealDateList[-1]
		return filterData.compareTo(Date.parse("yyyy.MM.dd",dealDateList[-1])) == 1
	}
}