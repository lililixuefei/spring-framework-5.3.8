package com.xuefei.spring;

import com.xuefei.spring.config.AppConfig;
import com.xuefei.spring.service.HelloService;
import com.xuefei.spring.spring.MyClassPathXmlApplicationContext;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: xuefei
 * @date: 2022/03/05 02:33
 */
public class SpringTest {
	public static void main(String[] args) {
		ApplicationContext context = new MyClassPathXmlApplicationContext(AppConfig.class);
//		ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
//		XmlBeanFactory spring-framework-5.3.8

		HelloService bean = context.getBean(HelloService.class);
		bean.hello();
	}
}
