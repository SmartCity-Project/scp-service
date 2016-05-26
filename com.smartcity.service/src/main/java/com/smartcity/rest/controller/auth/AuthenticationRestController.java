/**
 * 
 */
package com.smartcity.rest.controller.auth;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcity.business.controllers.impl.OrganizationController;
import com.smartcity.business.repositories.impl.OrganizationRepoImpl;
import com.smartcity.business.repositories.impl.RoleRepoImpl;
import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.business.security.authentication.token.IPasswordController;
import com.smartcity.data.access.Organization;
import com.smartcity.data.access.OrganizationRegister;
import com.smartcity.data.access.User;
import com.smartcity.data.access.UserLogin;
import com.smartcity.data.access.UserRegister;
import com.smartcity.rest.controller.crud.ArticleRestController;
import com.smartcity.rest.controller.crud.BaseRestController;

import org.springframework.dao.DuplicateKeyException;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationRestController
	extends BaseRestController
{	
	private Logger log = LoggerFactory.getLogger(AuthenticationRestController.class);

	@Autowired
	private UserRepoImpl userRepoImpl;
	
	@Autowired
	private RoleRepoImpl roleRepoImpl;
	
	@Autowired
	private OrganizationController organizationController;
	
	@Autowired
	private IPasswordController passwordControllerImpl;
	
	//TODO IMPROVE REGISTER LOGIC
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public Map<String, Object> register(@RequestBody String register) {
		getResponseMap().clear();

		String input = new String(Base64.decodeBase64(register));
		UserRegister userRegister = null;
		try {
			
			userRegister = new ObjectMapper().readValue(input, UserRegister.class);
//			&&userRegister.getPassword()==null
			if(userRegister.getEmail()==null) {
				throw new JsonMappingException("email attribute must not be null");
			}			
			
			User u = new User();
			
			if(userRegister.getOrganization()!=null) {
				u.addRole(roleRepoImpl.findOneByObjectId("ROLE_ADMIN"));	
			} else {
				u.addRole(roleRepoImpl.findOneByObjectId("ROLE_USER"));
			}
			
			Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
			
			u.setName(userRegister.getFirstName());
			u.setSurname(userRegister.getLastName());
			u.setUsername(userRegister.getEmail());
			u.setEmail(userRegister.getEmail());
			u.setSalt(passwordControllerImpl.generateSalt());
			u.setId(md5PasswordEncoder.encodePassword(u.getEmail(), u.getSalt()));
			u.setPassword(passwordControllerImpl.encodePassword(userRegister.getPassword(), u.getSalt()));
			u.setAccountNonLocked(true);
			u.setCredentialsNonExpired(true);
			u.setEnabled(true);
			if(userRegister.isMobileUser()) {
				u.addMobileInfo(userRegister.getMobileInfo());
			}
			
			userRepoImpl.save(u);
			
			if(userRegister.getOrganization()!=null) {
				Organization o = new Organization();
				OrganizationRegister or = userRegister.getOrganization();
				o.setName(or.getName());
				o.setAddress(or.getAddress());
				
				o.addUser(u);
				organizationController.saveOrganization(o);
				
				u.setOrganization(o);
				
				userRepoImpl.save(u);
			}
	
			getResponseMap().put("username", u.getUsername());
			
			return getResponseMap();
			
		} catch (JsonParseException e) {
			getResponseMap().put("error", "the encrypted json is wrong");
			log.error(e.getMessage());
		}  catch (JsonMappingException e) {
			getResponseMap().put("error", "the encrypted json is wrong");
			log.error(e.getMessage());
		} catch (DuplicateKeyException e) {
			e.printStackTrace();
			getResponseMap().put("error", "email exist");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return getResponseMap();
	}
	
}
