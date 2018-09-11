package org.yancey.myhouse.gzf.collection.model

import org.yancey.myhouse.collection.model.BaseData

import groovy.transform.Canonical
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 公租房申请人家庭成员信息
 * @author yangsai
 *
 */
@Canonical
@TargetUrl(value = 'http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do\\?method=queryDetailLhc*', sourceRegion = '/none')
class ProposerFamilyInfo extends BaseData implements AfterExtractor{
	String GX
	String XM
	String SFZH
	String RUHSJ
	String QTXX
	String LHMC_ID
	
	@Override
	public void afterProcess(Page page) {
		println("ssssssssss=======")
		//		this.afterCrawler(page)
	}
}
