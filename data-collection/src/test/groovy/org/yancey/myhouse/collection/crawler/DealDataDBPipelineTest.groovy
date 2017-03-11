package org.yancey.myhouse.collection.crawler;

import org.yancey.myhouse.collection.crawler.url.discovery.Condition;
import org.yancey.myhouse.collection.model.DealData
import org.yancey.myhouse.db.DBUtil

import spock.lang.Specification

public class DealDataDBPipelineTest extends Specification {
	DealData dd = new DealData(houseNo:'12123', dealDate:Date.parse("yyyy-MM-dd", "2017-02-26"))
	DBPipeline dbp = new DealDataDBPipelineTest()
	
	def setupSpec() {
		DBUtil.dbName = 'test.db'
	}
	
	def "成交日期 < 指定日期,应该不能增加到数据库"() {
		given:
		Condition.dealDate = Date.parse("yyyy-MM-dd", "2017-02-28")
		expect:
		!dbp.canAdd(dd)
	}
	
	def "成交日期 = 指定日期,且数据库存在,应该不能增加到数据库"() {
		given:
		Condition.dealDate = Date.parse("yyyy-MM-dd", "2017-02-26")
		
		DBUtil.createTable(dd)
		DBUtil.add(dd)
		expect:
		!dbp.canAdd(dd)
		
		cleanup:
		DBUtil.dropTable(dd)
	}
	
	def "成交日期 = 指定日期,且数据库不存在,应该能增加到数据库"() {
		given:
		Condition.dealDate = Date.parse("yyyy-MM-dd", "2017-02-26")
		expect:
		dbp.canAdd(dd)
	}
	
	
}
