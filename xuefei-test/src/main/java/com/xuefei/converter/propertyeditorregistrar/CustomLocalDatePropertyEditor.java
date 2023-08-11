package com.xuefei.converter.propertyeditorregistrar;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:00
 */
public class CustomLocalDatePropertyEditor extends PropertyEditorSupport {

	private String formatter;

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		LocalDate parse = LocalDate.parse(text, DateTimeFormatter.ofPattern(formatter));
		System.out.println("-----LocalDateEditor-----");
		//转换后的值设置给PropertyEditorSupport内部的value属性
		setValue(parse);
	}

	public CustomLocalDatePropertyEditor(String formatter) {
		this.formatter = formatter;
	}
}
