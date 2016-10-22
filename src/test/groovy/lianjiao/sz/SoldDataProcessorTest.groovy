

package lianjiao.sz

import spock.lang.Specification
import us.codecraft.webmagic.ResultItems
import us.codecraft.webmagic.Spider
import us.codecraft.webmagic.Task
import us.codecraft.webmagic.pipeline.Pipeline

class SoldDataProcessorTest extends Specification {
	
	def "should processor right data in LianJia"() {
		given:
		String testUrl = 'http://sz.lianjia.com/chengjiao/105100384939.html'
		SoldData testData = new SoldData(
			// 城市
			city: '深圳',
			// 区域
			quyu: '宝安',
			// 街道
			jiedao: '西乡',
			// 小区名
			village: '碧海富通城四期',
			// 户型
			huxing: '3室2厅',
			// 面积
			mianji: '119',
			// 楼层
			louceng: '高楼层',
			//朝向
			chaoxiang: '西南',
			//建设年代
			buildTime: '2008',
			// 建筑类型：板楼
			buildType: '板楼',
			// 成交价（万）
			totalCost: '190',
			// 单价 
			price: '15918',
			// 成交时间
			jiaoyiTime: '2016.10.05',
			// 房源编号
			houseNo: '105100384939',
			// 页面地址
			url: testUrl
			)
		SoldData processData
		when:
		Spider.create(new SoldDataProcessor()).
				addUrl(testUrl).
				addPipeline({ResultItems resultItems, Task task ->
						processData = resultItems.get("soldData");
					} as Pipeline).
				thread(5).
				run();
		then:
		processData == testData
	}
	
	def "should processor right buildType when buildType is no des"() {
		given:
		String testUrl = 'http://sz.lianjia.com/chengjiao/SZ0000852081.html'
		SoldData testData = new SoldData(
			// 建筑类型：板楼
			buildType: '未知'
			)
		SoldData processData
		when:
		Spider.create(new SoldDataProcessor()).
				addUrl(testUrl).
				addPipeline({ResultItems resultItems, Task task ->
						processData = resultItems.get("soldData");
					} as Pipeline).
				thread(5).
				run();
		then:
		processData.buildType == testData.buildType
	}
}
