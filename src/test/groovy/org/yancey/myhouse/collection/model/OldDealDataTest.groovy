package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Ignore
import spock.lang.Specification
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

class OldDealDataTest extends Specification {
	
	PageModelPipeline pipe = Mock(PageModelPipeline)
	def setupSpec() {
	}
	
	@Ignore("旧测试代码")
	def "应该从交易详情页面抓取到预期的成交数据"() {
		given:
		String url = 'http://sz.lianjia.com/chengjiao/105100384939.html'
		DealData dealData
		when:
		DataCrawler.testCrawl(pipe, url)
		
		then:
		1 * pipe.process(_ as DealData, _ as Task) >> { DealData d, Task task->
			dealData = d
		}
		dealData != null
		// 城市
		dealData.city == '深圳'
		// 区域
		dealData.quyu == '宝安区'
		// 街道
		dealData.jiedao == '西乡'
		// 小区名
		dealData.village == '碧海富通城四期'
		// 户型
		dealData.huxing == '3室2厅'
		// 面积
		dealData.mianji == 119.36
		// 楼层
		dealData.louceng == '高楼层'
		//朝向
		dealData.chaoxiang == '西南'
		//建设年代
		dealData.buildTime == 2008
		// 建筑类型：板楼
		dealData.buildType == '板楼'
		// 成交价（万）
		dealData.totalCost == 190
		// 单价
		dealData.price == 15918
		// 成交时间
		dealData.dealDate == Date.parse('yyyy.MM.dd', '2016.10.05')
		// 房源编号
		dealData.houseNo == '105100384939'
		// 页面地址
		dealData.url == url
	}
	
	def cleanup() {
	}
	
	def cleanupSpec() {
	}
	
}
