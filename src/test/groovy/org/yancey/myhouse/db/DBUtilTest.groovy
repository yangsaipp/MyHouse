package org.yancey.myhouse.db

import groovy.sql.Sql

import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.collection.model.DealData

import spock.lang.Shared
import spock.lang.Specification

class DBUtilTest extends Specification {

	@Shared Sql sql = DBUtil.getSql()

	def setupSpec() {
		println 'Setup spec.'
		sql.execute("drop table if exists person")
		sql.execute("create table person (id integer, name string)")
	}

	def setup() {
		println 'setup'
	}

	def "should save data success."() {
		given:

		when:
		def people = sql.dataSet("person")
		people.add(id:1, name:"leo")
		people.add(id:2, name:'yui')
		List results = sql.rows("select * from person where id in (?, ?)", [1, 2]);
		def peopleNames = results.collect {it.name}.join(',')

		then:
		peopleNames == 'leo,yui'
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
//		DailySalesData data = new DailySalesData(city:'深圳',quyu: '罗湖区', jiedao: '百仕达', num: 429, date: Date.parse('yyyy-MM-dd', '2017-02-19'));
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
		
		DailySalesData data = new DailySalesData(city:'深圳',quyu: '罗湖区', jiedao: '百仕达', num: 429, date: Date.parse('yyyy-MM-dd', '2017-02-19'));
		DBUtil.createTable(data)
		def dailySalesData
		
		when:
		DBUtil.add(data)
		dailySalesData = sql.firstRow("select * from DailySalesData where city = ?", ['深圳'])
		
		then:
		dailySalesData.city == '深圳'
		dailySalesData.quyu == '罗湖区'
		dailySalesData.jiedao == '百仕达'
		dailySalesData.num == 429
		Date.parse('yyyy-MM-dd', dailySalesData.date) == Date.parse('yyyy-MM-dd', '2017-02-19')
		
		cleanup:
		DBUtil.dropTable(data)
	}

	def cleanup() {
		println 'clean up'
		sql.execute('delete from person')
	}

	def cleanupSpec() {
		println 'clean spec.'
		sql.execute("drop table if exists person")
		sql.close();
	}
}
