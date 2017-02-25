package org.yancey.myhouse.collection.crawler

import org.yancey.myhouse.db.DBUtil

import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.PageModelPipeline

public class DBPipeline<T> implements PageModelPipeline<T> {
	
	@Override
	public void process(T t, Task task) {
		DBUtil.add(t)
	}
}
