package com.yn.constant;

import org.springframework.stereotype.Component;

import com.yn.annotation.PropertyConfig;

@Component
public class AppConstants {
	
	@PropertyConfig("app.url")
	public static String url;
	
	@PropertyConfig("aa")
	public static String aa;

}
