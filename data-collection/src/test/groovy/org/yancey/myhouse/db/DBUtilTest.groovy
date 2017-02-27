package org.yancey.myhouse.db

import groovy.sql.Sql

import org.apache.commons.dbcp.BasicDataSource
import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.collection.model.DealData

import spock.lang.Shared
import spock.lang.Specification

class DBUtilTest extends Specification {
	
	@Shared Sql sql

	def setupSpec() {
		DBUtil.dbName = 'test.db'
		sql = DBUtil.getSql()
		sql.execute("drop table if exists Person")
		sql.execute("create table Person (id integer, name string)")
	}

	def setup() {
	}

	def "should execute sql file success."() {
		given:
		File sqlFile = new File(this.getClass().getClassLoader().getResource("init.sql").toURI());
		
		when:
		DBUtil.executeSqlFile(sqlFile)

		then:
		DBUtil.isExistTable('DealData')
		DBUtil.isExistTable('DailySalesData')
		
		cleanup:
		DBUtil.dropTable('DealData')
		DBUtil.dropTable('DailySalesData')
	}
	
	def "should save data success."() {
		given:
			
		when:
		def people = sql.dataSet("Person")
		people.add(id:1, name:"leo")
		people.add(id:2, name:'yui')
		List results = sql.rows("select * from Person where id in (?, ?)", [1, 2]);
		def peopleNames = results.collect {it.name}.join(',')
				
		then:
		peopleNames == 'leo,yui'
	}
	
	def "should query data success."() {
		given:
		def people = sql.dataSet("Person")
		people.add(id:1, name:"leo")
		when:
		List results = DBUtil.rows(new Person(id:1), 'id');
		def peopleNames = results.collect {it.name}

		then:
		peopleNames == ['leo']
	}
	
	def "should exist table."() {
		expect:
		DBUtil.isExistTable('person')
	}
	
	def "should drop table."() {
		expect:
		DBUtil.dropTable(DailySalesData)
	}

	def "should creat table success ."() {
		given:
		DailySalesData data = new DailySalesData(city:'深圳',quyu: '罗湖区', jiedao: '百仕达', num: 429, crawlerDate: Date.parse('yyyy-MM-dd', '2017-02-19'));
		
		when:
		boolean result = DBUtil.createTable(DealData)
		
		then:
		result == true
		DBUtil.isExistTable(DealData)
		
		cleanup:
		DBUtil.dropTable(DealData)
	}
	
	def "should add data success ."() {
		given:
		
		DailySalesData data = new DailySalesData(id:1,city:'深圳',quyu: '罗湖区', jiedao: '百仕达', num: 429, crawlerDate: Date.parse('yyyy-MM-dd', '2017-02-19'));
//		DBUtil.createTable(data)
		def dailySalesData
		
		when:
		DBUtil.add(data)
		dailySalesData = sql.firstRow("select * from DailySalesData where city = ?", ['深圳'])
		
		then:
		dailySalesData.city == '深圳'
		dailySalesData.quyu == '罗湖区'
		dailySalesData.jiedao == '百仕达'
		dailySalesData.num == 429
		Date.parse('yyyy-MM-dd', dailySalesData.crawlerDate) == Date.parse('yyyy-MM-dd', '2017-02-19')
		
		cleanup:
		DBUtil.dropTable(data)
	}

	def cleanup() {
		sql.execute('delete from Person')
	}

	def cleanupSpec() {
		sql.execute("drop table if exists Person")
		sql.close();
	}
}
