/**
 * 
 */
package com.smartcity.business.jobs;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.smartcity.business.controllers.impl.PetitionController;
import com.smartcity.data.common.Petition;
import com.smartcity.data.petition.PetitionStatusEnum;

/**
 * @author gperreas
 *
 */
@Controller
public class PetitionEndsByJobTask 
	implements Runnable 
{
	private Logger log = LoggerFactory.getLogger(PetitionEndsByJobTask .class);
	
	@Autowired
    private PetitionController petitionController;
	
	public PetitionEndsByJobTask() {
		
	}

    public void run() {
    	log.info("petition task started");
    	
    	Date now = new Date();
    	
    	petitionController.modifyPetitionStatusByEndsDateAndNotComplete(now, PetitionStatusEnum.completed);
    	
    	log.info("petition task ended");
    }

}
