package com.xuefei.custom.resolveBeforeInstantiation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 22:43
 */
public class ResolveBeforeInstantiationApp {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("xuefei/resolveBeforeInstantiation.xml");
		BeanInstantiation beanInstantiation = (BeanInstantiation) applicationContext.getBean("beanInstantiation");
		beanInstantiation.smile();
	}


}
