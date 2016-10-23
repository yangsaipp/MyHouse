package lianjiao.sz

class CSVFileMerger {
	static main(args) {
		String path = 'e:/data/baoan/sz.lianjia.com/'
		String suffix = '.csv'
		int num = 0
		File mergeFile = new File('e:/data/', 'merge-data-' + new Date().format('yyyy-MM-dd-HHmm') + suffix)
		mergeFile.withPrintWriter { pw ->
			pw.write('城市,区域,街道,小区名,户型,面积,楼层,朝向,建设年代,建筑类型,成交价（万）,单价,成交时间(月份),成交时间,房源编号,页面地址\r\n')
			new File(path).eachFileMatch(~/.*\.csv/) { file ->
				num ++     
			    pw.write(file.text)
			}
		}
		
		println "读取$num 个文件内容合并到文件：$mergeFile"
	}
}
