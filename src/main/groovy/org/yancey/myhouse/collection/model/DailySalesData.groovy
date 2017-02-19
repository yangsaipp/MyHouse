package org.yancey.myhouse.collection.model

import groovy.transform.Canonical
import us.codecraft.webmagic.Page
import us.codecraft.webmagic.model.AfterExtractor
import us.codecraft.webmagic.model.annotation.ExtractBy
import us.codecraft.webmagic.model.annotation.HelpUrl
import us.codecraft.webmagic.model.annotation.TargetUrl

/**
 * 每日可销售的房源总数
 * @author yangsai
 *
 */
@Canonical
@TargetUrl(value='http://*.lianjia.com/ershoufang/\\w+/', sourceRegion = '//div[@data-role="ershoufang"]/div[2]')
@HelpUrl(value='http://*.lianjia.com/ershoufang/\\w+/', sourceRegion = '//div[@data-role="ershoufang"]/div[1]')
class DailySalesData implements AfterExtractor {
	/** 城市  */
	@ExtractBy('//div[@class=\'contentBottom clear\']/div[@class=\'crumbs fl\']/a[2]/regex("<a.+>(.+)二手房</a>", 1)')
	String city
	/** 区域 */
	@ExtractBy('//div[@class=\'contentBottom clear\']/div[@class=\'crumbs fl\']/a[3]/regex("<a.+>([^,]+).*二手房</a>", 1)')
	String quyu
	/** 街道 */
	@ExtractBy('//div[@class=\'contentBottom clear\']/div[@class=\'crumbs fl\']/h1/a/regex("<a.+>(.+)二手房</a>", 1)')
	String jiedao
	/** 房源总数 */
	@ExtractBy('//div[@class=\'leftContent\']//h2[@class=\'total fl\']/span/regex("<span.*>\\s(\\d+)\\s</span>", 1)')
	Integer num 
	/** 日期 */
	Date date
	
	@Override
	public void afterProcess(Page page) {
		date = new Date().clearTime()
//		println page.getHtml().xpath('//div[@class=\'contentBottom clear\']/div[@class=\'crumbs fl\']/a[3]/regex("<a.+>(\\w+)[\\w,]*二手房</a>", 1)').get()
	}
}