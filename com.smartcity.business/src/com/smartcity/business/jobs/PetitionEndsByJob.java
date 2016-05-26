/**
 * 
 */
package com.smartcity.business.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

/**
 * @author gperreas
 *
 */
@Controller
public class PetitionEndsByJob 
{
	private Logger log = LoggerFactory.getLogger(PetitionEndsByJob.class);
	
	@Autowired
	private PetitionEndsByJobTask petitionEndsByJobTask;

    private TaskExecutor petitionEndsByJobExecutor;

    public PetitionEndsByJob(TaskExecutor taskExecutor) {
        this.petitionEndsByJobExecutor = taskExecutor;
    }

    @Scheduled(cron="0 0 0 * * *")
    public void executeJob() {
    	petitionEndsByJobExecutor.execute(petitionEndsByJobTask);
    }

}
