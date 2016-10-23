package lianjiao.sz

import groovy.transform.Canonical

/**
 * 销售数据
 * @author yangsai
 *
 */
@Canonical
class SoldData {
	// 城市
	String city
	// 区域
	String quyu
	// 街道
	String jiedao
	// 小区名
	String village
	// 户型
	String huxing
	// 面积
	String mianji
	// 楼层
	String louceng
	//朝向
	String chaoxiang
	//建设年代
	String buildTime
	// 建筑类型：板楼
	String buildType
	// 成交价（万）
	String totalCost
	// 单价 
	String price
	// 成交时间(月份) 例如：2015.04
	String jiaoyiMonth
	// 成交时间 例如：2015.04.22
	String jiaoyiTime
	// 房源编号
	String houseNo
	// 页面地址
	String url
	
	@Override
	boolean equals(Object obj) {
		if(obj && obj instanceof SoldData) {
			return this.toString() == obj.toString()
		}
		false
	}
}
