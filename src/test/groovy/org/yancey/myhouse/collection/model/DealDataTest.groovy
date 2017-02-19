package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Title
import us.codecraft.webmagic.Task

@Title("常规页面抓取成交数据(DealData)准确性测试")
class DealDataTest extends Specification {
	
	@Shared DealData dealData
	@Shared String url = 'http://sz.lianjia.com/chengjiao/105100384939.html'
	@Shared pipeline = { d, Task task->
		dealData = d
	}
	def setupSpec() {
		DataCrawler.testCrawl(pipeline, url)
	}
	
	def "数据不能为空"() {
		expect:
		dealData !=  null
	}
	
	def "城市"() {
		expect: 
		dealData.city == '深圳'
	}
	def "区域"() {
		// 区域
		expect: 
		dealData.quyu == '宝安区'
	}
	def "街道"() {
		// 街道
		expect: 
		dealData.jiedao == '西乡'
	}
	def "小区名"() {
		// 小区名
		expect: 
		dealData.village == '碧海富通城四期'
	}
	def "户型"() {
		// 户型
		expect: 
		dealData.huxing == '3室2厅'
	}
	def "面积"() {
		// 面积
		expect: 
		dealData.mianji == 119.36
	}
	def "楼层"() {
		// 楼层
		expect: 
		dealData.louceng == '高楼层'
	}
	def "朝向"() {
		//朝向
		expect: 
		dealData.chaoxiang == '西南'
	}
	def "建设年代"() {
		//建设年代
		expect: 
		dealData.buildTime == 2008
	}
	def "建筑类型：板楼"() {
		// 建筑类型：板楼
		expect: 
		dealData.buildType == '板楼'
	}
	def "成交价（万）"() {
		// 成交价（万）
		expect: 
		dealData.totalCost == 190
	}
	def "单价"() {
		// 单价
		expect: 
		dealData.price == 15918
	}
	def "成交时间"() {
		// 成交时间
		expect: 
		dealData.dealDate == Date.parse('yyyy.MM.dd', '2016.10.05')
	}
	def "房源编号"() {
		// 房源编号
		expect: 
		dealData.houseNo == '105100384939'
	}
	def "页面地址"() {
		// 页面地址
		expect: 
		dealData.url == url
	}
	
}
