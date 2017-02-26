package org.yancey.myhouse.collection.crawler;

import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.collection.model.DealData
import org.yancey.myhouse.collection.model.DealDataHelper
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Site
import us.codecraft.webmagic.Spider
import us.codecraft.webmagic.model.OOSpider
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DataCrawler {
	/**
	 * 抓取数据
	 */
	static void crawl(PageModelPipeline pipeline, String... url) {
		createSpider(pipeline, [DealData, DailySalesData, DealDataHelper]).addUrl(url).thread(5).run()
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
	
	private static Spider createSpider(PageModelPipeline pipeline, List<Class> classes) {
		return OOSpider.create(Site.me()
				.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
//				.setHttpProxyPool(getProxyPool(), true)
				.addCookie("sz.lianjia.com","lianjia_uuid", "1dc08041-1df5-4580-a679-6e52b299a4ab")
				.addCookie("sz.lianjia.com","_ga", "GA1.2.1982026924.1477054703")
				.addCookie("sz.lianjia.com","lianjia_ssid", "546e47f9-80ee-4aee-b3c0-8dd735dc09ee")
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
		
//		crawl(new DBPipeline<Object>(), 'http://sz.lianjia.com/ershoufang/luohuqu/')
//		crawl(new DBPipeline<Object>(), 'http://wh.lianjia.com/ershoufang/jiangan/')
		crawl(new DBPipeline<Object>(), 'http://sz.lianjia.com/chengjiao/futianqu/pg1/')
	}
}
