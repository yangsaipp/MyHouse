package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.collection.crawler.helper.DailySalesDataHelper
import org.yancey.myhouse.collection.crawler.url.discovery.DailySalesDataUrlDiscovery
import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.db.DBUtil

import spock.lang.Specification
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.pipeline.PageModelPipeline

class DataCrawlerTest extends Specification {

	def setupSpec() {
		DBUtil.dbName = 'data - test.db'
	}
	
	def "应该能从销售列表页面发现期望的URL并保存到数据库中"() {
		given:
		String url = 'http://sz.lianjia.com/ershoufang/luohuqu/'
		Boolean canAdd
		GroovySpy(DailySalesDataUrlDiscovery, global: true) {
			// Mock getAreaUrl方法，获取所有发现的URL用于测试，实际返回为空URL
			getAreaUrl(_ as Page) >> {Page page ->
				return []
			}
			// Mock getAreaUrl方法，获取所有发现的URL用于测试，实际返回为空URL
			getStreetUrl(_ as Page) >> {Page page ->
				def list = callRealMethod()
				return list[0];
			}
		}
		
		PageModelPipeline pipeline = Spy(DailySalesDBPipeline) {
			canAdd(_) >> {
				canAdd = callRealMethod()
			}
		}
		
		
		when:
		DataCrawler.testRealCrawl(new DailySalesDBPipeline() ,[DailySalesDataHelper, DailySalesData], url)
		then:
		canAdd
	}
}
