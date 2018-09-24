package org.yancey.myhouse.gzf.collection.model;

import org.yancey.myhouse.db.DBUtil

import groovy.transform.Canonical;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy
import us.codecraft.webmagic.model.annotation.TargetUrl;

/**
 * http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do?method=queryDetailLhc&lhmcId=3955884&waittype=2
 * @author yangsai
 *
 */
@Canonical
@TargetUrl(value = "http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do\\?method=queryDetailLhc*", sourceRegion = "/none") 
@groovy.util.logging.Log4j
public class ProposerInfoDetailVO implements AfterExtractor {

	/** 备案回执号 */
	@ExtractBy('//div[@class=\'listcontent_right\']/div[2]/div[@class=\'leader_intro1\']/tidyText()')
	String SHOULHZH
	
	/** 户籍 */
	@ExtractBy('//div[@class=\'listcontent_right\']/div[4]/div[@class=\'leader_intro1\']/tidyText()')
	String HJ
	
	String LHMC_ID
	
	List<ProposerFamilyInfo> lstFamily = [];
	
	@Override
	public void afterProcess(Page page) {
//		SHOULHZH = SHOULHZH.split("\n")[0].split("：")[1].trim();
		HJ = HJ.split("\n")[2].split("：")[1].trim();
		LHMC_ID = page.getUrl().regex("&lhmcId=(\\d+)")
		// 更新户籍
		// 获取申请人信息
		parseFamilyInfo(page, 1);
		DBUtil.execute("update ProposerInfo set HJ='${this.HJ}' where LHMC_ID='${this.LHMC_ID}'");
		
		lstFamily.each { ProposerFamilyInfo info ->
			info.save();
		}
	}

	private void parseFamilyInfo(Page page, int i) {
		
		String strInfo = page.getHtml().xpath("//div[@class=\'listcontent_right\']/div[${5+i}]/div[@class=\'leader_intro1\']/tidyText()")
		if(strInfo == null) {
			return;
		}
		ProposerFamilyInfo familyInfo = new ProposerFamilyInfo(LHMC_ID:LHMC_ID);
		String[] arrInfo = strInfo.split("\n");
		familyInfo.GX = arrInfo[0].split("：")[1].trim();
		familyInfo.XM = arrInfo[1].split("：")[1].trim();
		familyInfo.SFZH = arrInfo[2].split("：")[1].trim();
		familyInfo.RUHSJ = arrInfo[3].split("：").length < 2 ? null : arrInfo[3].split("：")[1].trim();
		familyInfo.QTXX = arrInfo[4].split("：").length< 2 ? null: arrInfo[4].split("：")[1].trim();
		lstFamily.add(familyInfo);
		parseFamilyInfo(page, i+1);
	}
}
