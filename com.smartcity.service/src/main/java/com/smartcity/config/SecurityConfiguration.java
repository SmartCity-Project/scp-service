
package com.smartcity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.smartcity.business.security.authentication.token.ITokenController;
import com.smartcity.business.security.authentication.token.IUserStatusController;
import com.smartcity.business.security.filter.CORSFilter;
import com.smartcity.business.security.filter.LoginFilter;
import com.smartcity.business.security.filter.TokenFilter;
import com.smartcity.business.security.handler.LoginFailureHandler;
import com.smartcity.business.security.handler.LoginSuccessHandler;
import com.smartcity.business.security.handler.TokenLogoutHandler;
import com.smartcity.business.security.handler.TokenLogoutSuccessHandler;
import com.smartcity.business.security.handler.TokenSuccessHandler;
import com.smartcity.business.security.provider.PasswordAuthenticationProvider;
import com.smartcity.business.security.provider.TokenAuthenticationProvider;

/**
 * @author gperreas
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled = true)
public class SecurityConfiguration
	extends WebSecurityConfigurerAdapter
{
	@Autowired
	private UserDetailsManager userDetailsManager;
	
	@Autowired
	private TokenAuthenticationProvider tokenAuthenticationProvider;
	
	@Autowired
	private PasswordAuthenticationProvider passwordAuthenticationProvider;

	@Autowired
	private ITokenController tokenController;
	
	@Autowired
	private IUserStatusController userStatusController;
	
	@Autowired
	private TokenSuccessHandler tokenSuccessHandler;
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	private LoginFailureHandler loginFailureHandler;
	
	@Autowired 
	private TokenLogoutHandler tokenLogoutHandler;
	
	private static boolean disableDefaults = false;
	
	private static final String[] UNSECURED_RESOURCE_LIST =
            new String[]{"/*.html", "/**/*.html", "/*.css", "/*.js", "/index.html"};

	
    public SecurityConfiguration() {
		super(disableDefaults);
	}

	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public TokenFilter tokenFilter() throws Exception {
        TokenFilter tokenFilter = new TokenFilter();
        tokenFilter.setAuthenticationManager(authenticationManagerBean());
        tokenFilter.setTokenController(tokenController);
        tokenFilter.setAuthenticationSuccessHandler(tokenSuccessHandler);
        return tokenFilter;
    }
    
    @Bean
    public LogoutFilter logoutFilter() throws Exception {
    	TokenLogoutSuccessHandler logoutSuccessHandler = new TokenLogoutSuccessHandler(userStatusController);
    	
	    LogoutFilter logoutFilter = new LogoutFilter(logoutSuccessHandler, tokenLogoutHandler);
	    logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/api/auth/logout", "GET"));
        return logoutFilter;
    }
    
    @Bean
    public LoginFilter loginFilter() throws Exception {
    	LoginFilter loginFilter = new LoginFilter();
    	loginFilter.setAuthenticationManager(authenticationManagerBean());
    	loginFilter.setAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth", "POST"));
    	loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
    	loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return loginFilter;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider);
        auth.authenticationProvider(passwordAuthenticationProvider);
        auth.userDetailsService(userDetailsManager);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(UNSECURED_RESOURCE_LIST);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.requiresChannel()
//        		.anyRequest()
//        		.requiresSecure()
//        		.and()    	
    	http.addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class)
    		.httpBasic()
    			.disable()
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/**").authenticated()
                .and()
            .addFilterBefore(tokenFilter(), LogoutFilter.class)
            .addFilterAfter(loginFilter(), TokenFilter.class);
    }
}
