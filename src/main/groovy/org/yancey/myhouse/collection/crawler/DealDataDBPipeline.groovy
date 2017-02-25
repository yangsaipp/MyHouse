package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DealDataDBPipeline<DealData> implements PageModelPipeline<DealData> {
	
	@Override
	public void process(DealData dealData, Task task) {
		Date filterData = Date.parse("yyyy.MM.dd", "2017.01.06")
		if(dealData.dealDate == filterData && DBUtil.isExist(dealData, 'houseNo')) {
			return
		}
		DBUtil.add(dealData)
	}
}
