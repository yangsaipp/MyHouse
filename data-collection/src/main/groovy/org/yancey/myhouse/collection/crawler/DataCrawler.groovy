package org.yancey.myhouse.collection.crawler;

import org.yancey.myhouse.collection.crawler.helper.DailySalesDataHelper
import org.yancey.myhouse.collection.crawler.helper.DealDataHelper
import org.yancey.myhouse.collection.crawler.url.discovery.Condition
import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.collection.model.DealData
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Site
import us.codecraft.webmagic.model.OOSpider
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DataCrawler {
	/**
	 * 抓取数据
	 */
	static void crawl(PageModelPipeline pipeline, String... url) {
		createSpider(pipeline, [DealData, DailySalesData, DealDataHelper]).addUrl(url).thread(7).run()
	}
	
	
	static void ljCrawl(String... url) {
		createSpider(new DealDataDBPipeline(), [DealData, DealDataHelper])
		.addPageModel(new DailySalesDBPipeline(), DailySalesData, DailySalesDataHelper)
		.addUrl(url).thread(7).run()
	}
	
	/**
	 * 抓取数据
	 */
	static void testRealCrawl(PageModelPipeline pipeline,List<Class> classes, String... url) {
		createSpider(pipeline, classes).addUrl(url).thread(5).run()
	}
	
	/**
	 * 测试抓取数据
	 */
	static void testCrawl(PageModelPipeline pipeline, List<Class> classes, String... url) {
		createSpider(pipeline,classes).test(url)
	}
	
	private static OOSpider createSpider(PageModelPipeline pipeline, List<Class> classes) {
		return OOSpider.create(Site.me()
				.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
//				.setHttpProxyPool(getProxyPool(), true)
				.addCookie("sz.lianjia.com","lianjia_uuid", "5f559023-48b6-4343-8f0f-77f3f91adf7d")
//				.addCookie("sz.lianjia.com","_ga", "GA1.2.1982026924.1477054703")
				.addCookie("sz.lianjia.com","lianjia_ssid", "6ff0ba7c-9b20-4e43-a6bd-156352496bf6")
				
				.addCookie("wh.lianjia.com","lianjia_uuid", "5f559023-48b6-4343-8f0f-77f3f91adf7d")
				.addCookie("wh.lianjia.com","lianjia_ssid", "e2f0b99f-eb15-4a1f-bbf2-5e7202e419ab")
				, pipeline, classes as Class[])
	}
	
	static List<String[]> getProxyPool() {
		List<String[]> poolHosts = new ArrayList<String[]>();
		poolHosts.add(["","","127.0.0.1","8888"] as String[]);
		return poolHosts
	}
	
	static void main(String[] args) {
		DBUtil.dbName = 'data.db'
//		DBUtil.createTable(DealData, DailySalesData)
		// 成交数据
		Condition.dealPageNo = 30
//		ljCrawl('http://sz.lianjia.com/chengjiao/pg1/')
//		ljCrawl('http://wh.lianjia.com/chengjiao/pg1/')
		// 销售数据
		ljCrawl('http://sz.lianjia.com/ershoufang/luohuqu/','http://wh.lianjia.com/ershoufang/jiangan/')
//		ljCrawl()
	}
}
