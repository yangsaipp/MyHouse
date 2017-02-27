package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Title
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline


@Title("特殊页面（数据不完整）抓取成交数据(DealData)准确性测试")
class SpecialDealDataTest extends SpecialDataTest<DealData> {
	def classes = [DealData]
	def "信息不全的房屋信息应该能正确处理"() {
		when:
		testCrawl(classes, 'http://sz.lianjia.com/chengjiao/SZ0000852081.html')

		then: 
		crawlData != null
		crawlData.buildType == '板楼'
		crawlData.huxing == null
		crawlData.chaoxiang == '暂无数据'
	}
	
	def "无房屋构建年代的房屋信息应该能获取到建筑类型"() {
		when:
		testCrawl(classes, 'http://sz.lianjia.com/chengjiao/105100461210.html')

		then:
		crawlData != null
		crawlData.buildType == '板塔结合'
		crawlData.buildTime == null
	}
	
	def "成交价格带小数点的房屋信息应该能获取到"() {
		when:
		testCrawl(classes, 'http://sz.lianjia.com/chengjiao/105100384434.html')
			
		then:
		crawlData != null
		crawlData.totalCost == 93.5
	}
	
	def "房屋编号有字母的成交信息应该能获取到"() {
		when:
			testCrawl(classes, 'http://sz.lianjia.com/chengjiao/SZFT91463128.html')
			
			then:
				crawlData != null
				crawlData.houseNo == 'SZFT91463128'
	}
	
	
}
