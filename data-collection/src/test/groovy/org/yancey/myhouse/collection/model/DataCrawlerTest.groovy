package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Ignore;
import spock.lang.Specification

class DataCrawlerTest extends Specification {

	@Ignore
	def "运行DataCrawler"() {
		expect:
		DataCrawler.crawl({data, tesk-> println data}, 'http://sz.lianjia.com/ershoufang/baishida/')
	}
}
