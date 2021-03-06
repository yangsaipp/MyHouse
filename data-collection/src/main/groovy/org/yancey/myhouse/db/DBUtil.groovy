package org.yancey.myhouse.db

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import java.sql.SQLException

import javax.sql.DataSource

import org.apache.commons.dbcp.BasicDataSource

import com.alibaba.fastjson.JSON




class DBUtil {
//	static def BasicDataSource dataSource = new BasicDataSource(driverClassName: 'org.sqlite.JDBC',
//		url: 'jdbc:sqlite:sample.db')
	static String dbName;
	
	private static def BasicDataSource dataSource 
	
	static String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS"
	
	static DataSource getSingleDataSource() {
		if(dataSource == null) {
			if(dbName == null) {
				throw new SQLException('请先设置数据库名.')
			}
			dataSource = new BasicDataSource(driverClassName: 'org.sqlite.JDBC',
				url: 'jdbc:sqlite:db/' + dbName)
		}
		return dataSource
	}
	
	static Sql getSql() {
		
//		println 'get sql instance'
		return new Sql(getSingleDataSource());
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
	
	static boolean executeSqlFile(File sqlFile) {
		execute(sqlFile.getText())
	}
	
	static boolean execute(String sql) {
		println sql;
		getSql().execute(sql);
	}
	
	static boolean isExist(object, String... fields) {
		return !rows(object, fields).isEmpty();
	}
	
	static List<GroovyRowResult> rows(object, String... fields) {
		StringBuilder buffer = new StringBuilder("select * from ");
		String table = getTableName(object)
		List param = []
		buffer.append(table);
		if(fields) {
			buffer.append(" where ");
			boolean first = true;
			for(String field : fields) {
				if(first) {
					first = false
				}else {
					buffer.append(" and ");
				}
				if(object[field] instanceof Date) {
					buffer.append(" datetime($field) = ? ");
					param << object[field].format('yyyy-MM-dd HH:mm:ss')
				} else {
					buffer.append(" $field = ? ");
					param << object[field]
				}
				
			}
		}
		StringBuilder paramBuffer = new StringBuilder();
		
		List<GroovyRowResult> result = getSql().rows(buffer.toString(), param)
		return result
	}
	
	static void add(object) {
		assert object != null
		def people = getSql().dataSet(getTableName(object))
		def objMap = JSON.parseObject(JSON.toJSONStringWithDateFormat(object, DATE_FORMATTER))
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
		def con = getSingleDataSource().getConnection()
		boolean result = con.getMetaData().getTables(null, null, getTableName(object), null).next();
		con.close()
		return result
	}
}
