package org.yancey.myhouse.db

import spock.lang.Shared
import spock.lang.Specification

class DBUtilTest extends Specification {
	
	@Shared sql = DBUtil.getSql()
	
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
		List results = sql.rows("select * from person where id in (?, ?)", [1,2]);
		def peopleNames = results.collect {it.name}.join(',')
		
		then:
		peopleNames == 'leo,yui'
		
	}
	
	def "should save Object success ."() {
		given:
		
		when:
		def people = sql.dataSet("person")
		people.add(id:1, name:"leo")
		people.add(id:2, name:'yui')
		List results = sql.rows("select * from person where id in (?, ?)", [1,2]);
		def peopleNames = results.collect {it.name}.join(',')
		
		then:
		peopleNames == 'leo,yui'
		
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
