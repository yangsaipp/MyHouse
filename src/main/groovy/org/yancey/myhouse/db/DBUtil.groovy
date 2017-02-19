package org.yancey.myhouse.db

import groovy.sql.Sql

import java.sql.SQLException

import org.apache.commons.dbcp.BasicDataSource

class DBUtil {
	static def BasicDataSource dataSource = new BasicDataSource(driverClassName: 'org.sqlite.JDBC',
		url: 'jdbc:sqlite:sample.db')
	
	static Sql getSql() {
		println 'get sql instance'
		return new Sql(dataSource);
	}
	
	static void withSql(Closure c) throws SQLException {
		Sql sql = null;
		try {
			sql = getSql();
			c.call(sql);
		} finally {
			if (sql != null) sql.close();
		}
	}
	
	static String save(object) {
		// TODO:保存数据
	}
}
