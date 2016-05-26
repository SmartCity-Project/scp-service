/**
 * 
 */
package com.smartcity;

import java.io.FileNotFoundException;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.smartcity.business.context.ApplicationFactoriesRepository;
import com.smartcity.business.repositories.impl.AggregatedTagsRepoImpl;
import com.smartcity.business.repositories.impl.RoleRepoImpl;
import com.smartcity.business.repositories.impl.TagRepoImpl;
import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.business.security.authentication.PasswordControllerImpl;
import com.smartcity.business.security.filter.LoginFilter;
import com.smartcity.business.security.filter.TokenFilter;
import com.smartcity.business.tagging.TagController;
import com.smartcity.data.access.Role;
import com.smartcity.data.access.User;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
@Controller
@SpringBootApplication
@EnableScheduling
@ImportResource({"classpath*:applicationContext-mongo.xml",
				 "classpath*:applicationContext-security.xml",
				 "classpath*:applicationContext-jobs.xml"})
public class Application {
	
	public static void main(String[] args) throws Exception {
				
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		new ApplicationFactoriesRepository(context);
		
		Database.init("com.smartcity.business.repositories.impl");
				
    }
	
//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer()  throws FileNotFoundException {
//		final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();
//
//		return new EmbeddedServletContainerCustomizer() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer factory) {
//				if (factory instanceof TomcatEmbeddedServletContainerFactory) {
//					TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
//					containerFactory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
//							@Override
//							public void customize(Connector connector) {
//								connector.setPort(8443);
//								connector.setSecure(true);
//								connector.setScheme("https");
//								Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
//					            proto.setSSLEnabled(true);
//					            proto.setKeystoreFile(absoluteKeystoreFile);
//					            proto.setKeystorePass(keystorePass);
//					            proto.setKeystoreType("PKCS12");
//					            proto.setKeyAlias("tomcat");
//							}
//						});
//				}
//			}
//		};
//	}
}
