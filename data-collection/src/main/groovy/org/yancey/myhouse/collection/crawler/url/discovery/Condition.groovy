package org.yancey.myhouse.collection.crawler.url.discovery;

import org.yancey.myhouse.db.DBUtil


public class Condition {
	
	/**
	 * 数据库里最新 成交时间
	 */
	volatile static Date dealDate = Date.parse("yyyy.MM.dd", "2016.01.01");
	
	volatile static Integer dealPageNo = 10;
	
	static init() {
//		DBUtil.withSql {
//			def result = sql.firstRow("select * from DealData order by dealDate DESC")
//			if(!result.isEmpty()) {
//				dealDate = Date.parse(DBUtil.DATE_FORMATTER, result.dealDate)
//			} else {
//				dealDate = Date.parse("yyyy.MM.dd", "2016.01.01");
//			}
//		}
//		dealPageNo = 10
	}
}
