package org.yancey.myhouse.collection.crawler;

import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.collection.model.DealData
import org.yancey.myhouse.db.DBUtil;

import us.codecraft.webmagic.Site
import us.codecraft.webmagic.Spider
import us.codecraft.webmagic.model.OOSpider
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DataCrawler {
	/**
	 * 抓取数据
	 */
	static void crawl(PageModelPipeline pipeline, String... url) {
		createSpider(pipeline, url).thread(5).run()
	}
	
	/**
	 * 测试抓取数据
	 */
	static void testCrawl(PageModelPipeline pipeline, String... url) {
		createSpider(pipeline, url).test(url)
	}
	
	private static Spider createSpider(PageModelPipeline pipeline, String... url) {
		return OOSpider.create(Site.me()
				.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
				, pipeline, DealData, DailySalesData)
				.addUrl(url)
	}
	
	static void main(String[] args) {
		DBUtil.createTable(DealData, DailySalesData)
//		crawl(new DBPipeline<Object>(), 'http://sz.lianjia.com/ershoufang/luohuqu/')
		crawl(new DBPipeline<Object>(), 'http://sz.lianjia.com/chengjiao/luohuqu/pg1/')
	}
}
