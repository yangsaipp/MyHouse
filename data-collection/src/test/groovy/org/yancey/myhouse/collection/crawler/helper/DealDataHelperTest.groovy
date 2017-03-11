package org.yancey.myhouse.collection.crawler.helper

import org.yancey.myhouse.collection.crawler.DataCrawler
import org.yancey.myhouse.collection.crawler.helper.DealDataHelper;
import org.yancey.myhouse.collection.crawler.url.discovery.AssignDate;
import org.yancey.myhouse.collection.crawler.url.discovery.AssignPageNo
import org.yancey.myhouse.collection.crawler.url.discovery.Condition
import org.yancey.myhouse.collection.model.DealData;
import org.yancey.myhouse.db.DBUtil

import spock.lang.Specification
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.pipeline.PageModelPipeline

class DealDataHelperTest extends Specification {
	
	String nextPageUrl
	List<String> detailUrl = []
	
	def setupSpec() {
//		DBUtil.dbName = 'data - test.db'
	}
	
	def "应该只发现给定的成交成交时间的成交数据页面"() {
		Condition.dealDate = Date.parse("yyyy.MM.dd", "2017.02.22");
		given:
		String url = 'http://sz.lianjia.com/chengjiao/luohuqu/pg01/'
		PageModelPipeline pipeline = Mock(PageModelPipeline)
		GroovySpy(AssignDate, global: true) {
			getNextPageUrl(_ as Page) >> {Page page ->
				nextPageUrl = callRealMethod()
//				println "getNextPageUrl"
				return null
			}
			
			getDetailUrl(_ as Page) >> {Page page ->
				detailUrl.addAll(callRealMethod())
//				println "getDetailUrl"
				return []
			}
		}
		
//		GroovySpy(DealDataHelper, global: true) {
			// https://code.google.com/archive/p/spock/issues/169
			// getter setter无法mock
//			getUrlDiscovery() >> {	
//				println '无效'
//			}
//		}
		
		when:
		DataCrawler.testRealCrawl(pipeline,[DealDataHelper, DealData], url)
		then:
		0 * pipeline.process(_,_)
		nextPageUrl == null
		detailUrl.size() == 9
	}
	
	def "应该只发现给定的成交列表页面翻页范围内的成交数据地址"() {
		// 指定成交列表页面翻页的范围
		// 如：给定http://sz.lianjia.com/chengjiao/luohuqu/pg15/,则在只抓取15和16页个列表页面的数据
		given:
		DBUtil.dbName = 'data - test.db'
		Condition.dealPageNo = 1;
			
		String url = 'http://sz.lianjia.com/chengjiao/luohuqu/pg15/'
		PageModelPipeline pipeline = Mock(PageModelPipeline)
		def assignDateSpy = GroovySpy(AssignPageNo, global: true) {
			getNextPageUrl(_ as Page) >> {Page page ->
				nextPageUrl = callRealMethod()
//				println "getNextPageUrl"
				return null
			}
			
			getDetailUrl(_ as Page) >> {Page page ->
				detailUrl.addAll(callRealMethod())
//				println "getDetailUrl"
				return []
			}
		}
		when:
		DataCrawler.testRealCrawl(pipeline,[DealDataHelper, DealData], url)
		then:
		0 * pipeline.process(_,_)
		nextPageUrl == 'http://sz.lianjia.com/chengjiao/luohuqu/pg16/'
		detailUrl.size() == 0
	}
}
