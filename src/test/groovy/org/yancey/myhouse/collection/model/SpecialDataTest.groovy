package org.yancey.myhouse.collection.model

import org.yancey.myhouse.collection.crawler.DataCrawler

import spock.lang.Specification
import spock.lang.Title
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline


@Title("特殊页面（数据不完整）抓取数据准确性测试")
class SpecialDataTest<T> extends Specification {
	T crawlData
	
	void testCrawl(classes, url) {
		DataCrawler.testCrawl({ T data, Task task-> crawlData = data } as PageModelPipeline, classes , url)
	}
	
}
