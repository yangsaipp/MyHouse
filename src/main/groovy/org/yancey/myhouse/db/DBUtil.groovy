package org.yancey.myhouse.db

import groovy.sql.Sql

import java.sql.SQLException

import org.apache.commons.dbcp.BasicDataSource

import com.alibaba.fastjson.JSON


class DBUtil {
//	static def BasicDataSource dataSource = new BasicDataSource(driverClassName: 'org.sqlite.JDBC',
//		url: 'jdbc:sqlite:sample.db')
	
	static def BasicDataSource dataSource = new BasicDataSource(driverClassName: 'org.sqlite.JDBC',
		url: 'jdbc:sqlite:test.db')
	
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
	
	static void add(object) {
		assert object != null
		def people = getSql().dataSet(getTableName(object))
		def objMap = JSON.parseObject(JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss.SSS"))
		people.add(objMap)
		
	}
	
	// 根据给定的对象的类型创建表
	static boolean createTable(Object... objects) {
		assert objects != null
		for(Object object : objects) {
			if(isExistTable(object)) {
				return true
			}
			def createTableStr = buildCreateTable(object);
			println "create table : $createTableStr"
			getSql().execute(createTableStr)
		}
		
		return true
	}
	
	static boolean dropTable(obj) {
		assert obj != null
		String table = getTableName(obj)
		String dropStr = "drop table if exists $table";
		println "drop table : $dropStr"
		getSql().execute(dropStr)
		return true
	}
	
	static String getTableName(Object obj) {
		if(obj instanceof String) {
			return obj
		}else {
			return obj.metaClass.theClass.name.split('\\.')[-1]
		}
	}
	
	static String buildCreateTable(Object obj) {
		StringBuilder buffer = new StringBuilder("create table ");
		String table = getTableName(obj)
		buffer.append(table);
		buffer.append(" (");
		StringBuilder paramBuffer = new StringBuilder();
		boolean first = true;
		for (MetaProperty mp : obj.metaClass.properties) {
			if(mp.name == 'class') {
				continue
			}
			if (first) {
				first = false;
			} else {
				buffer.append(", ");
			}
			buffer.append("${mp.name} ${mp.type.getName().split('\\.')[-1]}");
		}
		buffer.append(")");
		return buffer.toString();
	}
	
	static boolean isExistTable(object) {
		assert object != null
//		String queryStr = "SELECT COUNT(*) FROM sqlite_master where type='table' and name= ?"
		def con = dataSource.getConnection()
		boolean result = con.getMetaData().getTables(null, null, getTableName(object), null).next();
		con.close()
		return result
	}
}
