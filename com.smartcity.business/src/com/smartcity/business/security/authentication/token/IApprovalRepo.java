/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.smartcity.data.access.token.Approval;

/**
 * @author gperreas
 *
 */
public interface IApprovalRepo {
	
	boolean updateOrCreate(Collection<Approval> approvals);

    boolean updateExpiresAt(Date expiresAt, Approval approval);

    boolean deleteByUserIdAndClientIdAndScope(Approval approval);

    List<Approval> findByUserIdAndClientId(String userId, String clientId);

}
