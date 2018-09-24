package org.yancey.myhouse.gzf.collection.model

import org.yancey.myhouse.collection.model.BaseData
import org.yancey.myhouse.db.DBUtil

import groovy.transform.Canonical

/**
 * 公租房申请人家庭成员信息
 * @author yangsai
 *
 */
@Canonical
@groovy.util.logging.Log4j
class ProposerFamilyInfo extends BaseData {
	String GX
	String XM
	String SFZH
	String RUHSJ
	String QTXX
	String LHMC_ID
	
	@Override
	public void save() {
		if(!DBUtil.isExist(this, 'XM')) {
			super.save();
		}else {
			log.info("数据库存在相同家庭成员信息。姓名=${this.XM}, LHMC_ID=${this.LHMC_ID}");
		}
	}
}
