package com.xuefei.converter.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:04
 */
public class String2IntegerConverter implements Converter<String, Integer> {

	@Override
	public Integer convert(String s) {
		// 为了区分String和Integer能直接转换，使用翻十倍返回结果
		return Integer.valueOf(s) * 10;
	}

}
