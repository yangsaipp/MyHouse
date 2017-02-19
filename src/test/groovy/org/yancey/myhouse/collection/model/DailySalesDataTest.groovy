package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title
import us.codecraft.webmagic.Task


@Title("常规页面抓取每日可售房屋数据(DailySalesData)准确性测试")
class DailySalesDataTest extends Specification {
	
	@Shared DailySalesData dailySalesData
	@Shared String url = 'http://sz.lianjia.com/ershoufang/baishida/'
	@Shared pipeline = { DailySalesData d, Task task->
			dailySalesData = d
		}
	
	def setupSpec() {
		DataCrawler.testCrawl(pipeline, url)
	}
	
	def "数据不能为空"() {
		expect:
		dailySalesData !=  null
	}
	
	def "城市"() {
		expect: 
		dailySalesData.city == '深圳'
	}
	def "区域"() {
		// 区域
		expect: 
		dailySalesData.quyu == '罗湖区'
	}
	def "街道"() {
		// 街道
		expect: 
		dailySalesData.jiedao == '百仕达'
	}
	def "房源总数 "() {
		// 房源总数 
		expect: 
		dailySalesData.num == 429
	}
	
	def "抓取时间"() {
		// 抓取时间
		expect: 
		dailySalesData.date == Date.parse('yyyy-MM-dd', '2017-02-19')
	}
}
