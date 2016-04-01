package com.liyuncong.application.commontools;

import java.util.Map;

/**
 * 封装的一些对Map的常用操作
 * @author yuncong
 *
 */
public class Maps {
	/**
	 * 此函数用于增加map中指定的key的value，如果指定的key之前不存在，就
	 * 往map中添加一个entry，键值分别为key和increaseNum。
	 * @param map
	 * @param key
	 * @param increaseNum
	 */
	public static void increase(Map<String, Integer> map, 
			String key,	Integer increaseNum) {
		if (map.containsKey(key)) {
			Integer oldValue = map.get(key);
			Integer newValue = oldValue + increaseNum;
			map.put(key, newValue);
		} else {
			map.put(key, increaseNum);
		}
	}
}
