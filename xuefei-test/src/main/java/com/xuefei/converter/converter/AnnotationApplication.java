package com.xuefei.converter.converter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:05
 */
@ComponentScan("com.xuefei.converter.converter")
public class AnnotationApplication {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AnnotationApplication.class);
		String2IntegerDataConverterTest integerDataConverterTest = context.getBean("string2IntegerDataConverterTest", String2IntegerDataConverterTest.class);
		// 控制台 100
		System.out.println(integerDataConverterTest.getTen());
	}

	@Bean
	public ConversionService conversionService() {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		conversionService.addConverter(new String2IntegerConverter());
		return conversionService;
	}
}

