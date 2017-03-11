package org.yancey.myhouse.collection.crawler.helper

import org.yancey.myhouse.collection.crawler.DataCrawler
import org.yancey.myhouse.collection.crawler.helper.DailySalesDataHelper
import org.yancey.myhouse.collection.crawler.url.discovery.DailySalesDataUrlDiscovery
import org.yancey.myhouse.collection.model.DailySalesData;
import org.yancey.myhouse.db.DBUtil

import spock.lang.Specification
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.pipeline.PageModelPipeline

class DailySalesDataHelperTest extends Specification {
	
	def setupSpec() {
		DBUtil.dbName = 'data - test.db'
	}
	
	def "应该能从销售列表页面发现期望的URL"() {
		given:
		String url = 'http://sz.lianjia.com/ershoufang/luohuqu/'
		PageModelPipeline pipeline = Mock(PageModelPipeline)
		List<String> lstAreaUrl = []
		List<String> lstStreetUrl = []
		def urlDiscoverySpy = GroovySpy(DailySalesDataUrlDiscovery, global: true) {
			// discoverUrl方法不需要Mock，需要正常调用
//			discoverUrl(_ as Page) >> {Page page ->
//				lstAreaUrl.addAll(callRealMethod())
//				println 'discoverUrl - 123123123123'
//				return null
//			}
			// Mock getAreaUrl方法，获取所有发现的URL用于测试，实际返回为空URL
			getAreaUrl(_ as Page) >> {Page page ->
				lstAreaUrl.addAll(callRealMethod());
				return []
			}
			// Mock getAreaUrl方法，获取所有发现的URL用于测试，实际返回为空URL
			getStreetUrl(_ as Page) >> {Page page ->
				lstStreetUrl.addAll(callRealMethod());
				return [];
			}
		}
		when:
		DataCrawler.testRealCrawl(pipeline,[DailySalesDataHelper, DailySalesData], url)
		then:
//		1 * urlDiscoverySpy.getAreaUrl(_ as Page)	// 若使用方法调用匹配，则上面GroovySpy对应里mock的行为就会失效。
//		1 * urlDiscoverySpy.getStreetUrl(_ as Page)
		lstAreaUrl.size() == 10
		lstStreetUrl.size() == 15
	}
}