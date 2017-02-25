package org.yancey.myhouse.collection.model

import groovy.transform.Canonical
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.ExtractBy
import us.codecraft.webmagic.model.annotation.Formatter
import us.codecraft.webmagic.model.annotation.HelpUrl
import us.codecraft.webmagic.model.annotation.TargetUrl
import us.codecraft.webmagic.model.formatter.DateFormatter

/**
 * 链家成交数据
 * @author yangsai
 *
 */
@Canonical
@TargetUrl('http://*.lianjia.com/chengjiao/\\w+.html$')
class DealData extends BaseData implements AfterExtractor {
	/** 城市 */
	@ExtractBy('//div[@class=\'wrapper\']/div[@class=\'deal-bread\']/a[2]/regex("<a.+>(.+)二手房成交价格</a>", 1)')
	String city
	
	/** 区域 */
	@ExtractBy('//div[@class=\'wrapper\']/div[@class=\'deal-bread\']/a[3]/regex("<a.+>(.+)二手房成交价格</a>", 1)')
	String quyu
	
	/** 街道 */
	@ExtractBy('//div[@class=\'wrapper\']/div[@class=\'deal-bread\']/a[4]/regex("<a.+>(.+)二手房成交价格</a>", 1)')
	String jiedao
	
	/** 小区名 */
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/p/a[1]/text()')
	String village
	
	/** 户型   如：3室2厅*/
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="msg"]/span[@class="sp01"]/label/regex("<label.*>(\\d室\\d厅)</label>", 1)')
	String huxing
	
	/** 面积 */
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="msg"]/span[@class="sp03"]/label/regex("<label>([\\d\\.]+)平米</label>", 1)')
	Double mianji
	
	/** 楼层 如：高楼层、中楼层*/
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="msg"]/span[@class="sp01"]/regex("<label>.+</label>(.+)\\(.*", 1)')
	String louceng
	
	/** 朝向  如：东南*/
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="msg"]/span[@class="sp02"]/label/text()')
	String chaoxiang
	
	//建设年代
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="msg"]/span[@class="sp03"]/regex("<label.*>.+</label>(\\d+)年建(.+)</span>", 1)')
	Integer buildTime
	
	/** 建筑类型 如：板楼 */
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="msg"]/span[@class="sp03"]/regex("<label.*>.+</label>(?:\\d+年建)?(.+)</span>", 1)')
	String buildType
	
	/** 成交价（万） */
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="price"]/span[@class="dealTotalPrice"]/i/text()')
	Double totalCost
	
	/** 单价  */
	@ExtractBy('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="price"]//@class="dealTotalPrice"/b/text()')
	Double price
	
	/** 成交时间 */
	@Formatter(value = "yyyy.MM.dd",formatter = DateFormatter)
	@ExtractBy('//div[@class=\'house-title\']/div[@class="wrapper"]/span/regex("<span.*>([\\d\\.]+)\\s链家成交</span>",1)')
	Date dealDate
	
	/** 房源编号 */
	// <span class="house-code">链家编号：105100384939</span>
	@ExtractBy('//div[@class=\'wrapper\']/div[@class=\'deal-bread\']/span[@class="house-code"]/regex("链家编号：(\\d+)", 1)')
	String houseNo
	
	/** 页面地址 */
	String url

	@Override
	public void afterProcess(Page page) {
		if(isOverTime()) {
			page.setSkip(true)
		}
		
		this.url = page.getUrl()
//		println page.getHtml().xpath('//div[@class=\'house-title\']/div[@class="wrapper"]/span/regex("<span.*>([\\d\\.]+)\\s链家成交</span>",1)').get() 
//		println page.getHtml().xpath('//div[@class=\'overview\']/div[@class="info fr"]/div[@class="price"]/span[@class="dealTotalPrice"]/i/text()').all() 
		this.afterCrawler()
		
		
	}
	
	/**
	 * 成交日期是否超出指定的时间
	 * @param page
	 * @return	true 是   false 否
	 */
	boolean isOverTime() {
		return Condition.dbDealDate.compareTo(dealDate) == 1
	}
}
