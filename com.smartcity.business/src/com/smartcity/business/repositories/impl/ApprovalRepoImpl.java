/**
 * 
 */
package com.smartcity.business.repositories.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;
import com.smartcity.business.security.authentication.token.IApprovalRepo;
import com.smartcity.data.access.token.Approval;

/**
 * @author gperreas
 *
 */
public class ApprovalRepoImpl
	extends BaseRepositoryImpl<Approval>
	implements IApprovalRepo
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Approval> getModelType() {

		return Approval.class;
	}

	@Override
	public boolean updateOrCreate(Collection<Approval> approvals) {

		boolean result = true;
        for (Approval a : approvals) {
            Update update = Update.update("expiresAt", a.getExpiresAt())
                    .set("status", a.getStatus())
                    .set("lastUpdatedAt", a.getLastUpdatedAt());

            WriteResult wr = mt.upsert(byUserIdAndClientIdAndScope(a), update, Approval.class);

            if (wr.getN() != 1) {
                result = false;
            }
        }
        return result;
	}

	@Override
	public boolean updateExpiresAt(Date expiresAt, Approval approval) {
		Update update = Update.update("expiresAt", expiresAt);

        WriteResult wr = mt.updateFirst(byUserIdAndClientIdAndScope(approval),
                update,
                Approval.class);

        return wr.getN() == 1;
	}

	@Override
	public boolean deleteByUserIdAndClientIdAndScope(Approval approval) {
		WriteResult wr = mt.remove(byUserIdAndClientIdAndScope(approval),
                Approval.class);

        return wr.getN() == 1;
	}

	@Override
	public List<Approval> findByUserIdAndClientId(String userId, String clientId) {
		
		Query query = Query.query(Criteria.where("userId").is(userId)
                .andOperator(Criteria.where("clientId").is(clientId)));
        return mt.find(query, Approval.class);
	}
	
	private Query byUserIdAndClientIdAndScope(Approval a) {
        return Query.query(Criteria.where("userId").is(a.getUserId())
                    .andOperator(Criteria.where("clientId").is(a.getClientId())
                            .andOperator(Criteria.where("scope").is(a.getScope()))));
    }

}
