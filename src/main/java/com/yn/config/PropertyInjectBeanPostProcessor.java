package com.yn.config;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.yn.annotation.PropertyConfig;

@Component
public class PropertyInjectBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {
	
	@Autowired
	private ConfigBean configBean;
	
	private SimpleTypeConverter converter = new SimpleTypeConverter();
	
	public boolean postProcessAfterInstantiation(Object bean, String beanName)
			throws BeansException {
		

		if("appConstants".equals(beanName)) {
			findPropertyAutowiringMetadata(bean);
		}
		
		return true;
	}
	
	private void findPropertyAutowiringMetadata(final Object bean) {
		
		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				PropertyConfig propertyConfig = field.getAnnotation(PropertyConfig.class);
				if(propertyConfig != null) {
					Object value = configBean.getProperty(propertyConfig.value());
					
					if(value != null) {
						value = converter.convertIfNecessary(value, field.getType());
						ReflectionUtils.makeAccessible(field);
						ReflectionUtils.setField(field, null, value);
					}
				}
			}
		});
		
	}

}
