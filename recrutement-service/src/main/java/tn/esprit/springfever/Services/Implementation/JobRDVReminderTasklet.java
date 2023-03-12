package tn.esprit.springfever.Services.Implementation;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Configurations.TwilioConfig;
import tn.esprit.springfever.entities.Job_RDV;
import tn.esprit.springfever.repositories.JobRdvRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class JobRDVReminderTasklet implements Tasklet {
    @Autowired
    JobRdvRepository jobRdvRepository;
    @Autowired
    TwilioConfig twilioConfig;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        // Get tomorrow's date
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // Get the list of Job_RDV objects scheduled for tomorrow
        List<Job_RDV> jobRDVs = jobRdvRepository.findJob_RDVByAppointmentDateBetween(
                LocalDateTime.of(tomorrow, LocalTime.MIN),
                LocalDateTime.of(tomorrow, LocalTime.MAX)
        );
        if(jobRDVs.isEmpty()){
            System.out.println("Pas de Rendez Vous Pour Demain");
        }

        // Loop through each Job_RDV object and send reminder SMS to candidate
        for (Job_RDV rdv : jobRDVs) {
            String candidatePhoneNumber = "+216 29 541 816";

            // Set reminder message
            String message = "Reminder: Your job interview is scheduled tomorrow at " + rdv.getAppointmentDate().toString() + ". Don't forget to bring your resume and portfolio.";

            // Send SMS using Twilio API
            Message.creator(new PhoneNumber(candidatePhoneNumber), new PhoneNumber(twilioConfig.getTwilioNumber()), message).create();
        }

        return RepeatStatus.FINISHED;
    }




}
