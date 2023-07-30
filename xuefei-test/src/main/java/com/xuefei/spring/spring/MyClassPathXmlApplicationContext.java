package com.xuefei.spring.spring;

import com.xuefei.spring.config.AppConfig;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @description:
 * @author: xuefei
 * @date: 2022/05/02 13:24
 */
public class MyClassPathXmlApplicationContext extends AnnotationConfigApplicationContext {


	public MyClassPathXmlApplicationContext(Class<AppConfig> appConfigClass) {
		super(appConfigClass);
	}

	@Override
	protected void initPropertySources() {
		super.initPropertySources();
	}



	@Override
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		beanFactory.addBeanPostProcessor(new MyAwareProcessor());
		super.prepareBeanFactory(beanFactory);
	}
}
