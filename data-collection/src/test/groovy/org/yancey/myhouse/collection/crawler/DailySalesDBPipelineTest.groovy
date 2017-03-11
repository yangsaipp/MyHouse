package org.yancey.myhouse.collection.crawler;

import org.yancey.myhouse.collection.crawler.url.discovery.Condition;
import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.db.DBUtil

import spock.lang.Specification

public class DailySalesDBPipelineTest extends Specification {
	DailySalesData dd = new DailySalesData(url:'http://sz.lianjia.com/ershoufang/baishida/', crawlerDate:Date.parse("yyyy-MM-dd", "2017-03-08"))
	DBPipeline dbp = new DailySalesDBPipeline()
	
	def setupSpec() {
		DBUtil.dbName = 'data - test.db'
	}
	
	def "当日已抓取的日销售的地区应该不能增加到数据库"() {
		given:
		
		expect:
		!dbp.canAdd(dd)
	}	
}
