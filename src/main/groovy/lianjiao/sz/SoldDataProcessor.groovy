package lianjiao.sz
import groovy.json.JsonSlurper
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.Site
import us.codecraft.webmagic.Spider
import us.codecraft.webmagic.pipeline.ConsolePipeline
import us.codecraft.webmagic.pipeline.JsonFilePipeline
import us.codecraft.webmagic.processor.PageProcessor

/*
 * 深圳链家二手房成交记录爬虫
 * @author yangsai, @date 16-10-21 下午9:30
 */
class SoldDataProcessor implements PageProcessor {
	private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
	.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
	.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
	.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
	.setCharset("UTF-8");
	
	static final String KEY_SOLDDATA = 'soldData'
	// 区域成交列表第一页url   如 宝安区所有http://sz.lianjia.com/chengjiao/baoan/
	public static final String AREA_URL_LIST_FIRST = "http://sz\\.lianjia\\.com/chengjiao/\\w+/"
	// 区域成交列表除第一页之外的url   如 宝安区所有http://sz.lianjia.com/chengjiao/baoan/pg4
	public static final String AREA_URL_LIST = "http://sz\\.lianjia\\.com/chengjiao/\\w+/pg\\d+"
	
	// 房屋成交信息 如：http://sz.lianjia.com/chengjiao/105100384939.html
//	public static final String URL_DETAIL = "http://sz\\.lianjia\\.com/chengjiao/\\d+\\.html"
	
	@Override
	public void process(Page page) {
//		List<String> relativeUrl = page.getHtml().xpath("//li[@class='item clearfix']/div/a/@href").all();
//		page.addTargetRequests(relativeUrl);
//		relativeUrl = page.getHtml().xpath("//div[@id='zh-question-related-questions']//a[@class='question_link']/@href").all();
//		page.addTargetRequests(relativeUrl);
//		List<String> answers =  page.getHtml().xpath("//div[@id='zh-question-answer-wrap']/div").all();
//		boolean exist = false;
//		for(String answer:answers){
//			String vote = new Html(answer).xpath("//div[@class='zm-votebar']//span[@class='count']/text()").toString();
//			if(Integer.valueOf(vote) >= voteNum){
//				page.putField("vote",vote);
//				page.putField("content",new Html(answer).xpath("//div[@class='zm-editable-content']"));
//				page.putField("userid", new Html(answer).xpath("//a[@class='author-link']/@href"));
//				exist = true;
//			}
//		}
//		if(!exist){
//			page.setSkip(true);
//		}
		
		if(page.getUrl().regex(AREA_URL_LIST).match()) {	// 列表页面非第一页
			// 加入当前列表页面里的详情连接到待分析列表
			addDetailUrl(page)
			page.setSkip(true);
		}else if(page.getUrl().regex(AREA_URL_LIST_FIRST).match()) {	// 列表页面第一页
			// 加入当前列表页面里的详情连接到待分析列表
			addDetailUrl(page)
			// 加入本列表页面翻页url
			addPageListUrl(page)
			page.setSkip(true);
		}else {	// 详情页面
			// 处理详情页面
			processDetailsPage(page);
		}
	}
	
	// 加入翻页url
	void addPageListUrl(Page page) {
		// 翻页jsonString
		String jsonString = page.getHtml().xpath("//div[@class=\"leftContent\"]//div[@class='page-box house-lst-page-box']/@page-data").toString()
		def pageNoInfo = new JsonSlurper().parseText(jsonString)
		int curPage = pageNoInfo.curPage
		int totalPage = pageNoInfo.totalPage
		curPage ++ // 从第二页开始
		List<String> pageSizeUrl = []
		while(curPage <= totalPage) {
			pageSizeUrl << page.getUrl().toString() + "pg$curPage"
			curPage ++
		}
		page.addTargetRequests(pageSizeUrl);
	}
	
	// 加入当前列表页面里的详情连接到待分析列表
	void addDetailUrl(Page page) {
		page.addTargetRequests(page.getHtml().xpath("//div[@class=\"leftContent\"]/ul[@class='listContent']/li/a[@class='img']/@href").all());
	}

	void processDetailsPage(Page page) {
		SoldData soldData = new SoldData()
		//　房屋编号
		soldData.houseNo = page.getHtml().xpath('//div[@class=\'wrapper\']/div[@class=\'deal-bread\']/span[@class=\'house-code\']/text()').toString().split('：')[1]
		if(!soldData.houseNo) {
			page.setSkip(true);
		}
		
		soldData.city = '深圳'
		List<String> areaInfos = page.getHtml().xpath('//div[@class=\'info fr\']/p/a/text()').all()
		// 区域
		soldData.quyu = areaInfos[1]
		// 街道
		soldData.jiedao = areaInfos[2]
		// 小区名
		soldData.village = areaInfos[0]
		
		List<String> houseInfos = page.getHtml().xpath('//div[@class=\'info fr\']/div[@class=\'msg\']/span/label/text()').all()
		// 户型：3室2厅
		soldData.huxing = houseInfos[0]
		// 面积
		soldData.mianji = houseInfos[2] - '平米'
		// 朝向
		soldData.chaoxiang = houseInfos[1]
		
		houseInfos = page.getHtml().xpath('//div[@class=\'info fr\']/div[@class=\'msg\']/span/text()').all()
		
		// 楼层
		soldData.louceng = houseInfos[0].substring(0, houseInfos[0].indexOf('('))	// 高楼层(共33层)
		// 建设年代
		soldData.buildTime = houseInfos[2].substring(0,4)	// 2008年建板楼
		// 建筑类型：板楼
		if(houseInfos[2].length() > 6) {
			soldData.buildType = houseInfos[2].substring(6, houseInfos[2].length()) // 2008年建板楼
		} else {
			soldData.buildType = '未知'
		}
		
		// 总价
		soldData.totalCost = page.getHtml().xpath('//div[@class=\'price\']/span/i/text()').toString()
		// 单价
		soldData.price = page.getHtml().xpath('//div[@class=\'price\']/b/text()').toString()
		// 交易时间
		soldData.jiaoyiTime = page.getHtml().xpath('//div[@class=\'house-title\']/div[@class=\'wrapper\']/span/text()').toString().split(' ')[0]
		
		// 成交时间(月份) 例如：2015.04
		soldData.jiaoyiMonth = soldData.jiaoyiTime.substring(0, 7)
		
		soldData.url = page.getUrl().toString()
		
		page.putField(KEY_SOLDDATA, soldData);
	}
	
	@Override
	public Site getSite() {
		return site;
	}

	static main(args) {
		Spider.create(new SoldDataProcessor()).
				addUrl('http://sz.lianjia.com/chengjiao/baoan/').
				addPipeline(new CSVFilePipeline('e:/data/baoan')).
//				addPipeline(new ConsolePipeline()).
				thread(5).
				run();
	}
}