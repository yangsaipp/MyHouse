package org.yancey.myhouse.gzf.collection;

import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import org.yancey.myhouse.db.DBUtil
import org.yancey.myhouse.gzf.collection.model.ProposerFamilyInfo
import org.yancey.myhouse.gzf.collection.model.ProposerInfo
import org.yancey.myhouse.gzf.collection.model.ProposerInfoDetailVO
import org.yancey.myhouse.gzf.collection.model.ProposerInfoPageVO

import us.codecraft.webmagic.Request
import us.codecraft.webmagic.Site
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.model.OOSpider
import us.codecraft.webmagic.pipeline.PageModelPipeline
import us.codecraft.webmagic.utils.HttpConstant

public class DataCrawler {
	
	
	static void gzfCrawl(Request... requests) {
		createSpider(new PageModelPipeline<Object>() {
			@Override
			public void process(Object t, Task task) {
				//
			}
		}, [ProposerInfoDetailVO, ProposerInfoPageVO])
//		.addPageModel(new MyDBPipeline<ProposerInfoPageVO>(), ProposerInfoPageVO)
		.addRequest(requests).thread(10).run()
	}
	
	private static OOSpider createSpider(PageModelPipeline pipeline, List<Class> classes) {
		return OOSpider.create(Site.me()
				.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36")
//				.setHttpProxyPool(getProxyPool(), false)
//				.addCookie("sz.lianjia.com","lianjia_uuid", "8b08238b-1831-4a28-99a4-8369c29cf286")
				, pipeline, classes as Class[])
	}
	
	static List<String[]> getProxyPool() {
		List<String[]> poolHosts = new ArrayList<String[]>();
		poolHosts.add(["","","127.0.0.1","8888"] as String[]);
		return poolHosts
	}
	
	static void main(String[] args) {
		DBUtil.dbName = 'gzf-data.db'
		DBUtil.createTable(ProposerInfo, ProposerFamilyInfo)
		String url = null;
		Map<String, Object> nameValuePair = new HashMap<String, Object>();
		NameValuePair[] values = new NameValuePair[7];
		values[0] = new BasicNameValuePair("pageNumber", "4");
		values[1] = new BasicNameValuePair("pageSize", "100");
		values[2] = new BasicNameValuePair("waittype", "2");
		values[3] = new BasicNameValuePair("num", "0");
		values[4] = new BasicNameValuePair("shoulbahzh", "");
		values[5] = new BasicNameValuePair("xingm", "");
		values[6] = new BasicNameValuePair("idcard", "");
		nameValuePair.put("nameValuePair", values);
		url = "http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do?method=queryYgbLhmcList";
//		url = "http://bzflh.szjs.gov.cn/TylhW/lhmcAction.do?method=queryDetailLhc&lhmcId=3956378&waittype=2";
		Request request = new Request(url);
		request.setExtras(nameValuePair);
		request.setMethod(HttpConstant.Method.POST);
		gzfCrawl(request);
//		ljCrawl()
	}
}
