package com.yn.config;

import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ConfigBean implements InitializingBean, FactoryBean<Object> {

	private Configuration configuration;

	private CompositeConfiguration compositeConfiguration;

	// InstantiationAwareBeanPostProcessorAdapter
	public Object getObject() throws Exception {
		return compositeConfiguration != null ? ConfigurationConverter
				.getProperties(configuration) : null;
	}

	public Class<?> getObjectType() {
		return Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {

		if (compositeConfiguration == null) {
			compositeConfiguration = new CompositeConfiguration();
		}

		compositeConfiguration.addConfiguration(configuration);

	}

	public Object getProperty(String key) {
		return compositeConfiguration.getString(key);
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

}
