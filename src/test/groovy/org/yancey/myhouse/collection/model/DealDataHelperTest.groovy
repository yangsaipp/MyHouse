package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Specification
import us.codecraft.webmagic.pipeline.PageModelPipeline

class DealDataHelperTest extends Specification {
	
	def "运行DataCrawler"() {
		given:
		String url = 'http://sz.lianjia.com/chengjiao/luohuqu/pg1/'
		PageModelPipeline pipeline = Mock(PageModelPipeline)
		def helperSpy = GroovySpy(DealDataHelper, global: true)
		when:
		DataCrawler.testRealCrawl(pipeline,[DealDataHelper, DealData], url)
		then:
		57 * pipeline.process(_,_)
		2 * helperSpy.addNextPage(_)
	}
}
