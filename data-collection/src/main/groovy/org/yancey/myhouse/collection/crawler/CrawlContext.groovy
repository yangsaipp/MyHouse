package org.yancey.myhouse.collection.crawler

class CrawlContext {
	
	static private Map<String, Object> map = new HashMap<String, Object>()
	
	static Object put(key, value) {
		return map.put(key, value)
	}
	
	static Object get(key) {
		return map.get(key)
	}
	
	static boolean contains(key) {
		return map.containsKey(key)
	}
	 
}
