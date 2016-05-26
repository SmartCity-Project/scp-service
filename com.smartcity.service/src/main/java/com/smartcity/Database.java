/**
 * 
 */
package com.smartcity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.smartcity.business.context.ApplicationFactoriesRepository;
import com.smartcity.business.repositories.IRepository;
import com.smartcity.business.repositories.impl.BaseRepositoryImpl;
import com.smartcity.business.repositories.impl.PermissionRepoImpl;
import com.smartcity.business.repositories.impl.ResourceRepoImpl;
import com.smartcity.business.repositories.impl.RoleRepoImpl;
import com.smartcity.business.repositories.impl.SequenceRepoImpl;
import com.smartcity.data.ISequenceId;
import com.smartcity.data.SequenceId;
import com.smartcity.data.access.Permission;
import com.smartcity.data.access.Resource;
import com.smartcity.data.access.Role;
import com.smartcity.data.common.Article;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.Petition;
import com.smartcity.data.common.ReportDocument;

/**
 * @author gperreas
 *
 */
public class Database {
	
	public static boolean initialize = false;

	public static boolean init(String packageName) throws ClassNotFoundException, IOException{
		
		initSequenceRepositiry();
				
		Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
		Set<Class<? extends BaseRepositoryImpl>> classes = reflections.getSubTypesOf(BaseRepositoryImpl.class);
		
		for(Class c:classes) {
			try {
				if(!c.getName().contains(".Base")) {
					IRepository<Object> r = 
							(IRepository<Object>) ApplicationFactoriesRepository.getContext().getBean(c);
					r.createCollection();
					
					initialize = true;
				}
			} catch(ClassCastException  e) {
				//swallow
			} catch(NullPointerException e) {
				e.printStackTrace();
			}
		}
 		
		initSequenceRepositiry();
		
		initRBACEntries();
		
		return initialize;
	}
	
	private static void initSequenceRepositiry() throws ClassNotFoundException, IOException {
		
		SequenceRepoImpl sequenceRepoImpl = 
				ApplicationFactoriesRepository.getContext().getBean(SequenceRepoImpl.class);
		
		
		
		String packageName = "com.smartcity.data";
		//TODO get and subpackages
		Set<Class<?>> classes = findClasses(packageName);
		
		for(Class c:classes) {
		
			if(!c.isEnum()) {
				
				if(ISequenceId.class.isAssignableFrom(c)&&(!c.equals(ISequenceId.class))) {
					
					if(sequenceRepoImpl.findOneByObjectId(
							c.getCanonicalName())==null) {
						
						SequenceId sqid = new SequenceId();
						sqid.setId(c.getCanonicalName());
						sqid.setSeq(0L);
						sequenceRepoImpl.save(sqid);
					}
						
				}
			}	
		}
	}

	private static boolean initRBACEntries() throws ClassNotFoundException, IOException {
		
		PermissionRepoImpl permissionRepoImpl = ApplicationFactoriesRepository.getContext().getBean(PermissionRepoImpl.class);
		ResourceRepoImpl resourceRepoImpl = ApplicationFactoriesRepository.getContext().getBean(ResourceRepoImpl.class);
		RoleRepoImpl roleRepoImpl = ApplicationFactoriesRepository.getContext().getBean(RoleRepoImpl.class);
		
		Set<Role> roles = new HashSet<Role>();
		
		Role admin = new Role();
		admin.setId("ROLE_ADMIN");
		roles.add(admin);
		Role user = new Role();
		user.setId("ROLE_USER");
		roles.add(user);
		Role superAdmin = new Role();
		superAdmin.setId("ROLE_SUPER_ADMIN");
		roles.add(superAdmin);
		
		
		//packages
		Permission adminPermission = new Permission();
		adminPermission.setId("1");
		adminPermission.setName("resourceAccess");
		Permission userPermission = new Permission();
		userPermission.setId("2");
		userPermission.setName("partialResourceAccess");
		Permission superAdminPermission = new Permission();
		superAdminPermission.setId("3");
		superAdminPermission.setName("fullResourceAccess");

//		Set<Class<?>> classes = findClasses("gr.smartcity.data.common");
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(Article.class);
		classes.add(Event.class);
		classes.add(Petition.class);
		classes.add(ReportDocument.class);
		
		int counter = 1;
		for(Role role:roles) {
			Set<Resource> resources = new HashSet<Resource>();
			Set<Permission> permissions = new HashSet<Permission>();

			for(Class c:classes) {
				if(!c.isEnum()) {
					Resource r = new Resource();
					r.setId(String.valueOf(counter));
					r.setName(c.getCanonicalName());
					if(role.getId().equals("ROLE_SUPER_ADMIN")) {
						r.setReadable(true);
						r.setWritable(true);
						r.setCreatable(true);
						r.setModifiable(true);
					}
					else if(role.getId().equals("ROLE_ADMIN")) {
						r.setReadable(true);
						r.setWritable(true);
						r.setCreatable(true);
						r.setModifiable(true);
						
						if(c.equals(Article.class)) {
							r.setCreatable(true);
							r.setModifiable(true);
						} else if(c.equals(Event.class)) {
							r.setCreatable(false);
							r.setModifiable(true);
						} else if(c.equals(Petition.class)) {
							r.setCreatable(true);
							r.setModifiable(true);
						}
					}
					else if(role.getId().equals("ROLE_USER")) {
						r.setReadable(true);
						r.setWritable(true);
						r.setCreatable(true);
						r.setModifiable(true);
						
						if(c.equals(Article.class)) {
							r.setCreatable(false);
							r.setModifiable(false);
						} else if(c.equals(Event.class)) {
							r.setCreatable(true);
							r.setModifiable(true);
						} else if(c.equals(Petition.class)) {
							r.setCreatable(false);
							r.setModifiable(false);
						}						
					}
					resourceRepoImpl.save(r);
					
					resources.add(r);
					counter++;
				}
			}
			
			if(role.getId().equals("ROLE_SUPER_ADMIN")) {
				superAdminPermission.setRecources(resources);
				permissions.add(superAdminPermission);
				permissionRepoImpl.save(superAdminPermission);
			}
			else if(role.getId().equals("ROLE_ADMIN")) {
				adminPermission.setRecources(resources);
				permissions.add(adminPermission);
				permissionRepoImpl.save(adminPermission);
			}
			else if(role.getId().equals("ROLE_USER")) {
				userPermission.setRecources(resources);
				permissions.add(userPermission);
				permissionRepoImpl.save(userPermission);
			}
			role.setPermissions(permissions);
			roleRepoImpl.save(role);
		}
		
		return true;
	}
	
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     */
    static Set<Class<?>> findClasses(String packageName) throws ClassNotFoundException {

    	Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));

        return reflections.getSubTypesOf(Object.class);
    }
}
