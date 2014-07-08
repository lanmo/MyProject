package com.yn.config;

import java.net.URL;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ConfigBean implements InitializingBean,FactoryBean<ConfigBean> {
	
	private CompositeConfiguration compositeConfiguration;
	
	//InstantiationAwareBeanPostProcessorAdapter
	public ConfigBean getObject() throws Exception {
		
		return null;
	}

	public Class<?> getObjectType() {
		return null;
	}

	public boolean isSingleton() {
		return false;
	}

	public void afterPropertiesSet() throws Exception {
	}

}
