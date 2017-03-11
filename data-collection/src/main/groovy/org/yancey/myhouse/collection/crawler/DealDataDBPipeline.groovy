package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.collection.crawler.url.discovery.Condition;
import org.yancey.myhouse.collection.model.DealData
import org.yancey.myhouse.db.DBUtil

public class DealDataDBPipeline extends DBPipeline<DealData> {
	
	@Override
	boolean canAdd(DealData t) {
		if(DBUtil.isExist(t, 'houseNo')) {
			return false
		}
		return true
	}
}
