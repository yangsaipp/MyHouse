package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Ignore;
import spock.lang.Specification
import us.codecraft.webmagic.pipeline.PageModelPipeline

class DealDataHelperTest extends Specification {
	
	def "应该能根据给定的时间判断是否要进行翻页"() {
		Condition.dbDealDate = Date.parse("yyyy.MM.dd", "2016.09.19");
		given:
		String url = 'http://sz.lianjia.com/chengjiao/luohuqu/pg10/'
		PageModelPipeline pipeline = Mock(PageModelPipeline)
		def helperSpy = GroovySpy(DealDataHelper, global: true)
		when:
		DataCrawler.testRealCrawl(pipeline,[DealDataHelper], url)
		then:
		0 * pipeline.process(_,_)
		3 * helperSpy.addNextPage(_)
	}
}
