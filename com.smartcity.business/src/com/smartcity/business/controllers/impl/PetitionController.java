/**
 * 
 */
package com.smartcity.business.controllers.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.mongodb.DuplicateKeyException;
import com.smartcity.business.filter.PetitionSearchCriteria;
import com.smartcity.business.filter.SearchResult;
import com.smartcity.business.repositories.impl.AggregatedSignaturesRepoImpl;
import com.smartcity.business.repositories.impl.PetitionRepoImpl;
import com.smartcity.business.repositories.impl.SignatureRepoImpl;
import com.smartcity.business.tagging.TagController;
import com.smartcity.data.common.Petition;
import com.smartcity.data.petition.AggregatedPetitions;
import com.smartcity.data.petition.AggregatedSignatures;
import com.smartcity.data.petition.PetitionStatusEnum;
import com.smartcity.data.petition.Signature;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;

/**
 * @author gperreas
 *
 */
@Component
public class PetitionController
{
	@Autowired
	private PetitionRepoImpl petitionRepoImpl;
	
	@Autowired
	private SignatureRepoImpl signatureRepoImpl;
	
	@Autowired
	private AggregatedSignaturesRepoImpl aggregatedSignaturesRepoImpl;
	
	@Autowired
	private TagController tagController;
	
	public PetitionController() {
		
	}
	
	public PetitionController(PetitionRepoImpl petitionRepoImpl, 
			SignatureRepoImpl signatureRepoImpl, 
			AggregatedSignaturesRepoImpl aggregatedSignaturesRepoImpl,
			TagController tagController) {
		this.petitionRepoImpl = petitionRepoImpl;
		this.signatureRepoImpl = signatureRepoImpl;
		this.aggregatedSignaturesRepoImpl = aggregatedSignaturesRepoImpl;
		this.tagController = tagController;
	}
	
	public AggregatedSignatures signToPetition(Signature signature) {
		Assert.notNull(signature);
		Signature existSignature = signatureRepoImpl.findExistSignature(signature);
		AggregatedSignatures aggregatedSignatures = null;
		
		try {
			if(existSignature!=null) {

				if(existSignature.isCanceled()) {
					existSignature.setCanceled(false);
					signatureRepoImpl.updateSignatureCancel(existSignature);
					aggregatedSignatures = calculateSignatures(existSignature);
				}
				else {
					aggregatedSignatures = cancelSignature(existSignature);
				}
			}
			else {
				signature.setCanceled(false);
				signatureRepoImpl.save(signature);
				aggregatedSignatures = calculateSignatures(signature);
			}
		} catch(DuplicateKeyException e) {
			
		}
		
		return aggregatedSignatures;
	}
	
	private AggregatedSignatures cancelSignature(Signature signature) {
		signature.setCanceled(true);
		
		AggregatedSignatures aggregatedSignatures = calculateSignatures(signature);
		signatureRepoImpl.updateSignatureCancel(signature);
		
		return aggregatedSignatures;
	}

	private AggregatedSignatures calculateSignatures(Signature signature) {
		return this.aggregatedSignaturesRepoImpl.updateAggregatedSignatures(signature);
	}

	public Petition savePetition(Petition petition) {
		
		petitionRepoImpl.save(petition);
		petition.setPetitionStatus(PetitionStatusEnum.started);
		AggregatedSignatures as = new AggregatedSignatures();
		as.setDocumentId(petition.getId());
		
		aggregatedSignaturesRepoImpl.save(as);
		
		return petition;
	}
	
	public Petition updatePetition(Petition petition) {
		return petitionRepoImpl.update(petition);
	}
	
	public boolean deletePetition(Long petitionId) {
		return petitionRepoImpl.delete(petitionId);
	}
	
	public AggregatedSignatures getAggregatedSignaturesByPetitionId(Long petitionId) {
		return this.aggregatedSignaturesRepoImpl.findOneByObjectId(petitionId);
	}
	
	public Petition findPetitionById(Long id) {
		Assert.notNull(id);
		return this.petitionRepoImpl.findOneByObjectId(id);
	}
	
	public List<Petition> findListByEndsDateAndNotComplete(Date endBy) {
		Assert.notNull(endBy);
		return this.petitionRepoImpl.findListByMapKeyValue(
				Criteria.where("endBy").lte(endBy).andOperator(
						Criteria.where("petitionStatus").ne(PetitionStatusEnum.completed)));
	}

	public void modifyPetitionStatusByEndsDateAndNotComplete(Date endBy, PetitionStatusEnum status) {
		this.petitionRepoImpl.modifyPetitionStatusByEndsDateAndNotComplete(endBy, status);
	}
	
	public SearchResult<Petition> find(PetitionSearchCriteria criteria) {
		
		Set<Criteria> criteriaSet = new HashSet<Criteria>();
		
		if(criteria.getOrganizationId()!=null) {
			if(!criteria.getOrganizationId().isEmpty()) {
				criteriaSet.add(Criteria.where("organizationId").is(criteria.getOrganizationId()));
			}
		}
		
		if(criteria.getTitle()!=null) {
			if(!criteria.getTitle().isEmpty()) {
				criteriaSet.add(Criteria.where("title").regex(criteria.getTitle()));
			}
		}
		
		if(criteria.getPetitionStatus()!=null) {
			criteriaSet.add(Criteria.where("petitionStatus").is(criteria.getPetitionStatus()));
		}
		
		if(criteria.getTags()!=null) {
			if(!criteria.getTags().isEmpty()) {
				criteriaSet.add(Criteria.where("tags").in(criteria.getTags()));
			}
		}
		
		if(criteria.getEndBy()!=null) {
			criteriaSet.add(Criteria.where("endsBy").is(criteria.getEndBy()));
		}
		
		Sort sort = new Sort(Direction.DESC,"tracking.created");
		
		return petitionRepoImpl.findPageByMapKeyValue(criteriaSet, sort, criteria);
	}

	public long countPetitions() {
		return this.petitionRepoImpl.countPetitions();
	}
	
	public long countPetitions(String orgId) {	
		return this.petitionRepoImpl.countPetitions(orgId);
	}

}
