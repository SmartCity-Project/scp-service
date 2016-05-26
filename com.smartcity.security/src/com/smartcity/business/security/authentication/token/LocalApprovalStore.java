/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import com.smartcity.business.repositories.impl.ApprovalRepoImpl;

/**
 * @author gperreas
 *
 */
@Component
public class LocalApprovalStore 
	implements ApprovalStore
{
	private final Logger log = LoggerFactory.getLogger(LocalApprovalStore.class.getName());
	
    private ApprovalRepoImpl approvalRepo;

    private boolean handleRevocationsAsExpiry = false;
    
    @Autowired
    public LocalApprovalStore(ApprovalRepoImpl approvalRepository) {
    	this.approvalRepo = approvalRepository;    	
    }

	@Override
	public boolean addApprovals(Collection<Approval> approvals) {
		Collection<com.smartcity.data.access.token.Approval> mongoApprovals =
				Collections2.transform(approvals, toMongoApproval());

        return approvalRepo.updateOrCreate(mongoApprovals);
	}

	@Override
	public boolean revokeApprovals(Collection<Approval> collection) {
		boolean success = true;

        Collection<com.smartcity.data.access.token.Approval> approvals = 
        		Collections2.transform(collection, toMongoApproval());

        for (com.smartcity.data.access.token.Approval a : approvals) {
            if (handleRevocationsAsExpiry) {
                final boolean updateResult = approvalRepo.updateExpiresAt(new Date(), a);
                if (!updateResult) {
                    success = false;
                }
            }
            else {
                boolean deleteResult = approvalRepo.deleteByUserIdAndClientIdAndScope(a);

                if (!deleteResult) {
                    success = false;
                }
            }
        }
        return success;
	}

	@Override
	public Collection<Approval> getApprovals(String userId, String clientId) {
		List<com.smartcity.data.access.token.Approval> approvals = 
				approvalRepo.findByUserIdAndClientId(userId, clientId);
        return Collections2.transform(approvals, toApproval());
    }

	private Function<Approval, com.smartcity.data.access.token.Approval> toMongoApproval() {
        return new Function<Approval, com.smartcity.data.access.token.Approval>() {
            @Override
            public com.smartcity.data.access.token.Approval apply(Approval approval) {
            	
            	com.smartcity.data.access.token.Approval a =
            			new com.smartcity.data.access.token.Approval();
            	
            	a.setId(UUID.randomUUID().toString());
            	a.setUserId(approval.getUserId());
            	a.setClientId(approval.getClientId());
            	a.setScope(approval.getScope());
            	a.setStatus(approval.getStatus() == null ? Approval.ApprovalStatus.APPROVED: approval.getStatus());
            	a.setExpiresAt(approval.getExpiresAt());
            	a.setLastUpdatedAt(approval.getLastUpdatedAt());
            	
            	return a;
            }
        };
    }

    private Function<com.smartcity.data.access.token.Approval, Approval> toApproval() {
        return new Function<com.smartcity.data.access.token.Approval, Approval>() {
            @Override
            public Approval apply(com.smartcity.data.access.token.Approval approval) {
                return new Approval(approval.getUserId(),
                		approval.getClientId(),
                		approval.getScope(),
                		approval.getExpiresAt(),
                		approval.getStatus(),
                		approval.getLastUpdatedAt());
            }
        };
    }

	public boolean isHandleRevocationsAsExpiry() {
		return this.handleRevocationsAsExpiry;
	}

	public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
        this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
    }
    
}
