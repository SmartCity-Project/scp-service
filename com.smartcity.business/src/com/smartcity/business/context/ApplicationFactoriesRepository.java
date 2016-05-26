/**
 * 
 */
package com.smartcity.business.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.smartcity.exceptions.ApplicationException;

/**
 * @author gperreas
 *
 */
public class ApplicationFactoriesRepository {

	static ConfigurableApplicationContext context;
	
	public ApplicationFactoriesRepository(ConfigurableApplicationContext context) 
			throws ApplicationException {
		
		if(context==null)
			throw new ApplicationException("Application context must not be null");
		
		if(ApplicationFactoriesRepository.context==null)
			ApplicationFactoriesRepository.context=context;
	}
	
	public static ApplicationContext getContext() {
		return ApplicationFactoriesRepository.context;
	}
}
