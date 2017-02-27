package org.yancey.myhouse.db;

import groovy.json.JsonSlurper

public class Person {
	Integer id 
	String name 
	
	static void main(String[] args) {
		def pageNoInfo = new JsonSlurper().parseText('{"totalPage":100,"curPage":10}')
		int curPage = pageNoInfo.curPage
		int totalPage = pageNoInfo.totalPage
		assert curPage != totalPage
		if(curPage == totalPage){		// 最后一页
			println '骗人的吧'
		} else {
			println '没骗人的吧'
		}
	}
}
