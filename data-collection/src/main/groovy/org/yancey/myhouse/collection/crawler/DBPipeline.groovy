package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.collection.model.Condition
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DBPipeline<T> implements PageModelPipeline<T> {
	
	@Override
	public void process(T t, Task task) {
		if(canAdd(t)) {
			DBUtil.add(t)
		} 
	}
	
	boolean canAdd(T t) {
		if(t.hasProperty('dealDate')) {
			int result = Condition.dbDealDate.compareTo(t.dealDate)
			if(result == 1) {	// 比数据库最新成交时间晚，不处理
				return false
			} else if(result == 0 && DBUtil.isExist(t, 'houseNo')) { // 与数据库最新成交时间相同且数据库中存在对应数据，不处理
				return false
			}
		}
		return true
	}
}
