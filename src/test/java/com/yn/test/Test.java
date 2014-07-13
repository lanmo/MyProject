package com.yn.test;

import com.alibaba.fastjson.JSONObject;

public class Test {

	public static void main(String[] args) {
		JSONObject json = new JSONObject();
		json.put("action", "get");
		json.put("user", "yangnan");
		System.out.println(json.toString());
	}
}
