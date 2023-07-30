package com.xuefei.definition.bean;

import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/02/24 23:46
 */
@Component
public class Person {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
