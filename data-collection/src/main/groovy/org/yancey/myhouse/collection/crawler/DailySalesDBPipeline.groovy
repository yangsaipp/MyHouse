package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.collection.crawler.url.discovery.Condition;
import org.yancey.myhouse.collection.model.DailySalesData
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DailySalesDBPipeline extends DBPipeline<DailySalesData> {
	
	@Override
	boolean canAdd(DailySalesData t) {
		if(DBUtil.isExist(t, 'crawlerDate', 'url')) { // 若当日已经抓取就不保存到数据库
			return false
		}
		return true
	}
}
