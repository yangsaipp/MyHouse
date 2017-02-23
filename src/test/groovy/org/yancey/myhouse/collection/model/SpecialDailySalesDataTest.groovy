package org.yancey.myhouse.collection.model

import spock.lang.Title


@Title("特殊页面抓取每日可售房屋数据(DailySalesData)准确性测试")
class SpecialDailySalesDataTest extends SpecialDataTest<DailySalesData> {
	
	def "街道属于两个地区情况下应该以对应房屋详情页面所属地区为准"() {
		// 街道
		when:
		testCrawl('http://sz.lianjia.com/ershoufang/huaqiaocheng1/')
		then:
		crawlData.quyu == '南山区'
	}
	
	def "各区每日可销售房屋数据应该不被抓取"() {
		// 街道
		when:
		testCrawl('http://sz.lianjia.com/ershoufang/nanshanqu/')
		then:
		crawlData == null
		
		when:
		testCrawl('http://sz.lianjia.com/ershoufang/yantianqu/')
		then:
		crawlData == null
	}
}