package com.xuefei.converter.ropertyeditorsupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: xuefei
 * @date: 2023/08/11 23:57
 */
@Component
class TestDate {

	@Value("2022-11-13 12:06:00")
	private LocalDateTime dateTime;

	public LocalDateTime getDateTime() {
		return dateTime;
	}

}
