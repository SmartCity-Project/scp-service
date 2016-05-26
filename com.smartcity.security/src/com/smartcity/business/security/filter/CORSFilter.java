/**
 * 
 */
package com.smartcity.business.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gperreas
 *
 */
public class CORSFilter 
	implements Filter 
{
 
    @Override
    public void init(FilterConfig config) throws ServletException {}
 
    @Override
    public void doFilter(
    		ServletRequest req, 
    		ServletResponse res,
            FilterChain chain) throws IOException, ServletException {

    	HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", LoginFilter.HEADER_AUTHORIZATION+","+
				TokenFilter.HEADER_TOKEN_KEY +",Content-Type,Accept,Origin"); 
        response.setHeader("Access-Control-Expose-Headers", TokenFilter.HEADER_TOKEN_KEY +",Content-Type");
        
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }
 
    @Override
    public void destroy() {}
 
}
