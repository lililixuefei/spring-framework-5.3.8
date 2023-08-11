package com.xuefei.converter.propertyeditorregistrar;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:01
 */
@ComponentScan("com.xuefei.converter.propertyeditorregistrar")
public class AnnotationApplication {

	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AnnotationApplication.class);
		TestDate testDate = context.getBean("testDate", TestDate.class);
		// 控制台  2022-11-13
		System.out.println(testDate.getDate());
	}

	@Bean
	public CustomEditorConfigurer customEditorConfigurer() {
		CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
		customEditorConfigurer.setPropertyEditorRegistrars(new PropertyEditorRegistrar[]{new CustomPropertyEditorRegistrar()});
		return customEditorConfigurer;
	}
}
