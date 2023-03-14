package tn.esprit.springfever.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.batch.BatchLauncher;

@Component
@Slf4j
@AllArgsConstructor
public class Scheduler {

    @Autowired
    private BatchLauncher jobLauncher;

    @Autowired
    private Job projectJob;

    @Scheduled(cron = "0 0 8 * * *")
    public void runProjectJob() throws Exception {
        System.out.println("Starting Project Job...");
        jobLauncher.run();
        System.out.println("Project Job Completed.");
    }
}
