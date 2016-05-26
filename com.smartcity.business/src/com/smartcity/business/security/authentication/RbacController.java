/**
 * 
 */
package com.smartcity.business.security.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.smartcity.business.context.ApplicationFactoriesRepository;
import com.smartcity.business.repositories.impl.RoleRepoImpl;
import com.smartcity.data.access.Permission;
import com.smartcity.data.access.Resource;
import com.smartcity.data.access.Role;

/**
 * @author gperreas
 *
 */
public class RbacController {
	
	private static RbacController instance;
	
	private static Map<String, Map<String, Permission>> rolePermissionsMap;
		
	private static Map<String, Map<String, Resource>> roleResourcesMap;

	
	public static RbacController getInstance() {
		if(instance==null) {
			instance = new RbacController();
			
			rolePermissionsMap = new HashMap<String, Map<String, Permission>>();
			
			roleResourcesMap = new HashMap<String, Map<String, Resource>>();

			init();
		}
		
		return instance;
	}

	private static void init() {
		RoleRepoImpl roleRepoImpl = ApplicationFactoriesRepository.getContext().getBean(RoleRepoImpl.class);
		List<Role> roles = roleRepoImpl.findAll();
		
		rolePermissionsMap.clear();
		roleResourcesMap.clear();
		
		for (Role role : roles) {
			Map<String, Permission> permissionsMap = new HashMap<String, Permission>();
			Map<String, Resource> resourcesMap = new HashMap<String, Resource>();
			for (Permission permission : role.getPermissions()) {
				permissionsMap.put(permission.getName(), permission);
				
				for (Resource resource : permission.getRecources()) {					
					resourcesMap.put(resource.getName(), resource);
				}
			}
			rolePermissionsMap.put(role.getId(), permissionsMap);
			roleResourcesMap.put(role.getId(), resourcesMap);
		}
	}
	
	public Resource hasResourceAccess(String documentName) {	
		
		GrantedAuthority authority = SecurityContextHolder.getContext()
							 .getAuthentication().getAuthorities().iterator().next();
		
		return roleResourcesMap.get(authority.getAuthority()).get(documentName);
	}
	
}
