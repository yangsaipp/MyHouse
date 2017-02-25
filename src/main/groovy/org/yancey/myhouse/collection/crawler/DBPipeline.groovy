package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.collection.model.Condition
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DBPipeline<T> implements PageModelPipeline<T> {
	
	@Override
	public void process(T t, Task task) {
		Date filterData = Date.parse("yyyy.MM.dd", "2017.01.06")
		if(t.dealDate && t.dealDate == Condition.dbDealDate && DBUtil.isExist(t, 'houseNo')) {
			return
		}
		DBUtil.add(t)
	}
}
