package com.xuefei.definition;

import com.xuefei.definition.config.BeanDefinitionQuickstartConfiguration;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/02/24 23:45
 */
public class BeanDefinitionQuickstartBeanApplication {

	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
				BeanDefinitionQuickstartConfiguration.class);
		BeanDefinition personBeanDefinition = ctx.getBeanDefinition("person");
		System.out.println(personBeanDefinition);
		System.out.println(personBeanDefinition.getClass().getName());

		System.out.println("----");
	}

}