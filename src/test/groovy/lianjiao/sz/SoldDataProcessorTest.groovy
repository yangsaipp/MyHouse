

package lianjiao.sz

import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor
import spock.lang.Specification
import us.codecraft.webmagic.Page
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
			jiaoyiMonth: '2016.10',
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
						processData = resultItems.get(SoldDataProcessor.KEY_SOLDDATA);
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
						processData = resultItems.get(SoldDataProcessor.KEY_SOLDDATA);
					} as Pipeline).
				thread(5).
				run();
		then:
		processData.buildType == testData.buildType
	}
	
	def "should add expect list page url to processor when specified deal date"() {
		given:
		String testUrl = 'http://sz.lianjia.com/chengjiao/baoan/'
		String dealDate = '2016.09'
		List<String> urlList = []
//		def pageMock = new StubFor(Page)
//		pageMock.demand.addTargetRequest { 
//			urlList << it
//		}
//		pageMock.ignore('getUrl')
//		pageMock.ignore('getHtml')
//		pageMock.ignore('setSkip')
		
		Page.metaClass.invokeMethod = { String name, args ->
//			println "invoke: $name"
			// 代理 Page的addTargetRequest方法
			if(name == 'addTargetRequest') {
				urlList << args[0]
			}
			def validMethod = Page.metaClass.getMetaMethod(name, args);
			if(validMethod) {
				validMethod.invoke(delegate, args)
			}else {
				Page.metaClass.invokeMissingMethod(delegate, name, args)
			}
		}
		def processorMock = new StubFor(SoldDataProcessor)
		// 宝安9月份成交数据199条,翻页8次
		processorMock.demand.addDetailUrl(8) { println "add deal detail page of list page url ${it.getUrl()} " }
		processorMock.ignore('addNextPageListUrl')
		when:
		processorMock.use {
//			pageMock.use {
				Spider.create(new SoldDataProcessor(filterDealDate:dealDate)).
				addUrl(testUrl).
				addPipeline({ResultItems resultItems, Task task ->
						if(resultItems.skip) {
							return;
						}
					} as Pipeline).
				thread(5).
				run();
//			}
		}
		then:
		groovy.test.GroovyAssert.assertArrayEquals(urlList as String[], ['http://sz.lianjia.com/chengjiao/baoan/pg2',
			'http://sz.lianjia.com/chengjiao/baoan/pg3',
			'http://sz.lianjia.com/chengjiao/baoan/pg4',
			'http://sz.lianjia.com/chengjiao/baoan/pg5',
			'http://sz.lianjia.com/chengjiao/baoan/pg6',
			'http://sz.lianjia.com/chengjiao/baoan/pg7',
			'http://sz.lianjia.com/chengjiao/baoan/pg8'
			] as String[])
//		processDataList.each {
//			assert it.jiaoyiMonth == '2016.10'
//		}
	}
}
