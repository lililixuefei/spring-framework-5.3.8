package com.xuefei.converter.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:05
 */
@Component
public class String2IntegerDataConverterTest {

	// 注意这里使用的是 10，应该转为 10，但是我们转换的时候让他翻了10倍，所以他现在应该是 100
	@Value("10")
	public Integer ten;

	public Integer getTen() {
		return ten;
	}

}
