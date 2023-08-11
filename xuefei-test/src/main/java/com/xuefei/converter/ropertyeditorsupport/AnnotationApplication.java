package com.xuefei.converter.ropertyeditorsupport;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.beans.PropertyEditor;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/11 23:58
 */
@ComponentScan("com.xuefei.converter.ropertyeditorsupport")
public class AnnotationApplication {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AnnotationApplication.class);
		TestDate testDate = context.getBean("testDate", TestDate.class);
		// 控制台  2022-11-13T12:06
		System.out.println(testDate.getDateTime());

	}

	@Bean
	public CustomEditorConfigurer customEditorConfigurer(){
		CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
		Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<Class<?>, Class<? extends PropertyEditor>>(6);
		customEditors.put(LocalDateTime.class, CustomDatePropertyEditor.class);
		customEditorConfigurer.setCustomEditors(customEditors);
		return customEditorConfigurer;
	}
}

