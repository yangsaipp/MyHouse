package org.yancey.myhouse.gzf.collection;

import org.yancey.myhouse.collection.crawler.DBPipeline;
import org.yancey.myhouse.collection.model.BaseData;

public class MyDBPipeline<T extends BaseData> extends DBPipeline<BaseData> {

	@Override
	public boolean canAdd(BaseData t) {
		return false;
	}
	

}
