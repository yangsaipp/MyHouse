package org.yancey.myhouse.gzf.collection.model;

import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.codehaus.groovy.transform.GroovyASTTransformation

import groovy.json.JsonSlurper;
import groovy.transform.Canonical;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import us.codecraft.webmagic.utils.HttpConstant

/**
 * http://www.szjs.gov.cn/bsfw/zdyw_1/zfbz/gxfgs/
 * @author yangsai
 *
 */
@Canonical
@TargetUrl(value = "http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do\\?method=queryYgbLhmcList*", sourceRegion = "/none")
@groovy.util.logging.Log4j
public class ProposerInfoPageVO implements AfterExtractor {
	int total;
	List<ProposerInfo> rows;
	
	@Override
	public void afterProcess(Page page) {
		JsonSlurper slurper = new groovy.json.JsonSlurper()
		ProposerInfoPageVO pageInfo = slurper.parseText(page.getRawText());
//		ProposerInfoPageVO pageInfo = page.getJson().toObject(ProposerInfoPageVO)
//		println(pageInfo.total);
//		println(pageInfo.rows);
		Request request = getNextPageUrl(page, pageInfo.total);
		if(request != null) {
			page.addTargetRequest(request);
		}
		List<String> lstDetailUrl = [];
		for(Object row : pageInfo.rows) {
			ProposerInfo info = new ProposerInfo();
			info.SQB_ID = row.SQB_ID
			info.LHCYXXB_ID = row.LHCYXXB_ID
			info.RZQK = row.RZQK
			info.PAIX = row.PAIX
			info.SHOUCCBSJ_GZ = row.SHOUCCBSJ_GZ
			info.QUA_DATE = row.QUA_DATE
			info.SHOULHZH = row.SHOULHZH
			info.SFZH = row.SFZH
			info.NUM = row.NUM
			info.RGQK = row.RGQK
			info.SHOUCCBSJ = row.SHOUCCBSJ
			info.OUTLH_FLAG = row.OUTLH_FLAG
			info.RUHSJ = row.RUHSJ
			info.SHOUCCBSJ_AJ = row.SHOUCCBSJ_AJ
			info.XINGM = row.XINGM
			info.OUT_TIME = row.OUT_TIME
			info.LHMC_ID = row.LHMC_ID
			info.WAIT_TPYE = row.WAIT_TPYE
			info.REMARK = row.REMARK
			info.HJ = row.HJ
			
			
			info.afterCrawler(page)
			// 保存数据
			info.save();
			// 将详情页面加入待请求的列表中
			lstDetailUrl << "http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do?method=queryDetailLhc&lhmcId=${info.LHMC_ID}&waittype=2".toString();
		}
		page.addTargetRequests(lstDetailUrl);
	}
	
	Request getNextPageUrl(Page page, int total) {
		NameValuePair[] requestValues = page.getRequest().getExtra("nameValuePair");
		int pageNumber = Integer.valueOf(requestValues[0].getValue())
		int pageSize = Integer.valueOf(requestValues[1].getValue())
		int totalPageNumber = total / pageSize;
		if(pageNumber >= totalPageNumber) {
			return null;
		}
		int nextPage = pageNumber + 1;
		log.info("增加下一页到抓取URL中。下一页：${nextPage}");
		Map<String, Object> nameValuePair = new HashMap<String, Object>();
		NameValuePair[] values = new NameValuePair[7];
		values[0] = new BasicNameValuePair("pageNumber", String.valueOf(nextPage));
//		requestValues[0] = new BasicNameValuePair("pageNumber", String.valueOf(nextPage));
		values[1] = new BasicNameValuePair("pageSize", String.valueOf(pageSize));
		values[2] = new BasicNameValuePair("waittype", "2");
		values[3] = new BasicNameValuePair("num", "0");
		values[4] = new BasicNameValuePair("shoulbahzh", "");
		values[5] = new BasicNameValuePair("xingm", "");
		values[6] = new BasicNameValuePair("idcard", "");
		nameValuePair.put("nameValuePair", values);
		Request request = new Request("http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do?method=queryYgbLhmcList&pageNumber=${nextPage}");
		request.setExtras(nameValuePair);
		request.setMethod(HttpConstant.Method.POST);
		request.setPriority(Long.MAX_VALUE);
		return request;
	}
	
}
