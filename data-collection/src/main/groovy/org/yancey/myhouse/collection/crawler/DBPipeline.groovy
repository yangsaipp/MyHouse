package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.collection.model.BaseData
import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

@groovy.util.logging.Log4j
public class DBPipeline<T extends BaseData> implements PageModelPipeline<T> {
	
	@Override
	public void process(T t, Task task) {
		if(canAdd(t)) {
			log.info("保存抓取数据，数据来源页面URL：${t.url}")
			DBUtil.add(t)
		} 
	}
	
	public boolean canAdd(T t) {
		return true
	}
}
