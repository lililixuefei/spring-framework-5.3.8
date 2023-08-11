package com.xuefei.converter.ropertyeditorsupport;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/11 23:57
 */
public class CustomDatePropertyEditor extends PropertyEditorSupport {

	private String formatter = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		LocalDateTime parse = LocalDateTime.parse(text, DateTimeFormatter.ofPattern(formatter));
		System.out.println("-----LocalDateTimeEditor-----");
		//转换后的值设置给PropertyEditorSupport内部的value属性
		setValue(parse);
	}

}
