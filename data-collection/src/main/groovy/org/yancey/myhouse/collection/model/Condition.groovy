package org.yancey.myhouse.collection.model;

import org.yancey.myhouse.db.DBUtil


public class Condition {
	
	/**
	 * 数据库里最新 成交时间
	 */
	volatile static Date dbDealDate = Date.parse("yyyy.MM.dd", "2016.01.01");
	
	static init() {
		DBUtil.withSql {
			def result = sql.firstRow("select * from DealData order by dealDate DESC")
			if(!result.isEmpty()) {
				dbDealDate = Date.parse(DBUtil.DATE_FORMATTER, result.dealDate)
			} else {
				dbDealDate = Date.parse("yyyy.MM.dd", "2016.01.01");
			}
		}
	}
}
