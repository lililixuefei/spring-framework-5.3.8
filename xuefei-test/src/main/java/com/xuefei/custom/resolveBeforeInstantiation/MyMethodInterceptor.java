package com.xuefei.custom.resolveBeforeInstantiation;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 22:41
 */
public class MyMethodInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
			throws Throwable {
		System.out.println("MyMethodInterceptor.interceptor(): " + method + "--->");
		/** 这里会调用具体的类的方法 */
		Object invokeSuper = methodProxy.invokeSuper(o, objects);
		System.out.println("<---MyMethodInterceptor.interceptor(): " + method);
		return invokeSuper;
	}


}
