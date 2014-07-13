package com.yn.test;

import java.util.HashMap;
import java.util.Map;

public class TestMain {
	
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		System.out.println(AppConstants.url);
		
		Map<String, A> map = new HashMap<>();
		A a = new A();
		a.b = "aaaa";
		map.put("a", a);
		
		A a1 = map.get("a");
		a1.b = "asd";
		System.out.println(map.get("a").b);
	}
	
	static class A {
		public String b;
	}
}
