package com.smartcity.business;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author gperreas
 *
 */
public class FactoriesRepository {

	static ConfigurableApplicationContext context  = initializeApplicationContext();
	
	private static ConfigurableApplicationContext initializeApplicationContext()
	{
		return new ClassPathXmlApplicationContext("classpath*:applicationContext-mongo.xml");
	}
	
	public static ConfigurableApplicationContext getContext() {
		return context;
	}

	public static ApplicationContext createChildFactory(String childFactoryXmlFile, ApplicationContext parentFactory)
	{
		if (childFactoryXmlFile == null) 
			throw new IllegalArgumentException("Argument 'childFactoryXmlFile' must not be null.");
		
		if (parentFactory == null) 
			throw new IllegalArgumentException("Argument 'parentFactory' must not be null.");
		
		String[] locations = { childFactoryXmlFile };
		
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext(locations, parentFactory);
		
		return context;
	}

}
