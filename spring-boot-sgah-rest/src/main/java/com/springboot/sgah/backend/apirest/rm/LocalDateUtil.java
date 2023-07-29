package com.springboot.sgah.backend.apirest.rm;

import java.time.LocalDate;

public class LocalDateUtil {

	public static int getMonth(LocalDate date) {

		if (date == null) {
			date = LocalDate.now();
		}

		return date.getMonthValue();
	}

	public static int getYear(LocalDate date) {
		
		if(date == null) {
			date = LocalDate.now();			
		}

		return date.getYear();
	}

}
