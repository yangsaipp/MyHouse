package org.yancey.myhouse.db

import groovy.sql.Sql

import spock.lang.Shared
import spock.lang.Specification

class DBUtilTest extends Specification {

	@Shared Sql sql = DBUtil.getSql()

	def setupSpec() {
		println 'Setup spec.'
		sql.execute("drop table if exists Person")
		sql.execute("create table Person (id integer, name string)")
	}

	def setup() {
		println 'setup'
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
//		DailySalesData data = new DailySalesData(city:'深圳',quyu: '罗湖区', jiedao: '百仕达', num: 429, date: Date.parse('yyyy-MM-dd', '2017-02-19'));
		when:
		boolean result = DBUtil.createTable(DealData)

		then:
		result == true
		DBUtil.isExistTable(DealData)
		
		cleanup:
		DBUtil.dropTable(DealData)
	}
	
	def cleanup() {
		println 'clean up'
		sql.execute('delete from Person')
	}

	def cleanupSpec() {
		println 'clean spec.'
		sql.execute("drop table if exists Person")
		sql.close();
	}
}
