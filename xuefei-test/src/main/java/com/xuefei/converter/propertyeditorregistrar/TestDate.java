package com.xuefei.converter.propertyeditorregistrar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/12 00:00
 */
@Component
class TestDate {

	@Value("2022年11月13日")
	private LocalDate date;

	public LocalDate getDate() {
		return date;
	}
}
