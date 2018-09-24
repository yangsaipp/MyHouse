package org.yancey.myhouse.gzf.collection.model

import org.yancey.myhouse.collection.model.BaseData
import org.yancey.myhouse.db.DBUtil

/**
 * 公租房申请人信息
 * @author yangsai
 *
 */
@groovy.util.logging.Log4j
class ProposerInfo extends BaseData{
	String SQB_ID
	String LHCYXXB_ID
	String RZQK
	String PAIX
	String SHOUCCBSJ_GZ
	String QUA_DATE
	/** 备案回执号 */
	String SHOULHZH
	String SFZH
	String NUM
	String RGQK
	String SHOUCCBSJ
	String OUTLH_FLAG
	String RUHSJ
	String SHOUCCBSJ_AJ
	String XINGM
	String OUT_TIME
	/** id */
	String LHMC_ID
	String WAIT_TPYE
	String REMARK
	/** 户籍 */
	String HJ
	
	@Override
	public void save() {
		if(!DBUtil.isExist(this, 'LHMC_ID')) {
			super.save();
		}else {
			log.info("数据库存在相同的申请人记录。LHMC_ID=${this.LHMC_ID}, 姓名=${this.XINGM}");
		}
	}
}
