package com.smartcity.business.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.smartcity.business.security.authentication.UserStatusControllerImpl;

import com.smartcity.data.access.User;
import com.smartcity.data.access.token.UserStatus;

@Component
public class TokenSuccessHandler 
	extends SimpleUrlAuthenticationSuccessHandler 
{

    @Autowired
    private UserStatusControllerImpl userStatusController;
    
    @Override
    public void onAuthenticationSuccess(
    		HttpServletRequest request, 
    		HttpServletResponse response, 
    		Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
        UserStatus userStatus = userStatusController.updateTokenAuthenticationSuccess(user, webAuthenticationDetails);

        response.addHeader("X-Auth-Token", userStatus.getTokenInfo().getToken());
        response.setStatus(HttpServletResponse.SC_OK);
        clearAuthenticationAttributes(request);
    }

}
