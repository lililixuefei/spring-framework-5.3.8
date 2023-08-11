package com.xuefei.converter.propertyeditorregistrar;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

import java.time.LocalDate;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:00
 */
public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

	@Override
	public void registerCustomEditors(PropertyEditorRegistry propertyEditorRegistry) {
		// 将字符串格式为 yyyy年MM月dd日 的转换为 LocalDate
		propertyEditorRegistry.registerCustomEditor(LocalDate.class, new CustomLocalDatePropertyEditor("yyyy年MM月dd日"));
	}

}
