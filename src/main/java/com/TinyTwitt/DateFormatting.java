package com.TinyTwitt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatting {
	
	private static DateFormatting dateFormatting = null; 
	
	public static synchronized DateFormatting getInstance() {
		if (null == dateFormatting) {
			dateFormatting = new DateFormatting();
		}
		return dateFormatting;
	}
	
	public String formatting(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return date.format(formatter);
	}
}
