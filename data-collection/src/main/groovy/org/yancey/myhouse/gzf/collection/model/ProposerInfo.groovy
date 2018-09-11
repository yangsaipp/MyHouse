package org.yancey.myhouse.gzf.collection.model

import org.yancey.myhouse.collection.model.BaseData

import com.alibaba.fastjson.TypeReference

import groovy.json.JsonSlurper
import groovy.transform.Canonical
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 公租房申请人信息
 * @author yangsai
 *
 */
@Canonical
@TargetUrl(value = 'http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do\\?method=queryYgbLhmcList', sourceRegion = '/none')
class ProposerInfo extends BaseData implements AfterExtractor{
	String SQB_ID
	String LHCYXXB_ID
	String RZQK
	String PAIX
	String SHOUCCBSJ_GZ
	String QUA_DATE
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
	String LHMC_ID
	String WAIT_TPYE
	String REMARK
	/** 户籍 */
	String HJ
	
	@Override
	public void afterProcess(Page page) {
		JsonSlurper slurper = new groovy.json.JsonSlurper()
		ProposerInfoPageVO pageInfo = slurper.parseText(page.getRawText());
//		ProposerInfoPageVO pageInfo = page.getJson().toObject(ProposerInfoPageVO)
		println(pageInfo.total);
		println(pageInfo.rows);
		for(ProposerInfo info : pageInfo.rows) {
			info.afterCrawler(page)
		}
	}
}
