package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Title
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline


@Title("特殊页面（数据不完整）抓取成交数据(DealData)准确性测试")
class SpecialDealDataTest extends SpecialDataTest<DealData> {
	
	
	def "信息不全的房屋信息应该能正确处理"() {
		when:
		testCrawl('http://sz.lianjia.com/chengjiao/SZ0000852081.html')

		then: 
		crawlData != null
		crawlData.buildType == '板楼'
		crawlData.huxing == null
		crawlData.chaoxiang == '暂无数据'
	}
}
