package tn.esprit.springfever.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.discovery.shared.Pair;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.Services.Implementation.JobRDVReminderTasklet;
import tn.esprit.springfever.Services.Interfaces.IJobRDV;
import tn.esprit.springfever.entities.Disponibilites;
import tn.esprit.springfever.entities.Job_RDV;
import tn.esprit.springfever.repositories.JobRdvRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@Configuration
public class JobRdvController {
    @Autowired
    IJobRDV iJobRDV;
    @Autowired
    JobRdvRepository jobRdvRepository;

   @Autowired
   JobRDVReminderTasklet jobRDVReminderTasklet; //Could not autowire. No beans of 'JobRDVReminderTasklet' type found.






    @PostMapping("addJobRDV/")
    public Job_RDV addJobRDV(@RequestBody Job_RDV job_rdv){
        return iJobRDV.addJobRDV(job_rdv);

    }



    @GetMapping("getAllJobRDVS/")
    public List<Job_RDV> getAllJobRDVs() {
        return iJobRDV.getAllJobRDVs();

    }

    /*@PutMapping("updateJobRDV/{id}")
    public Job_RDV updateJobRDV (@PathVariable("id") Long ID_Job_DRV , @RequestBody Job_RDV job_rdv ) {
        return iJobRDV.updateJobRDV(ID_Job_DRV,job_rdv);

    }*/
//updateJobRDV(Long ID_Job_DRV, Job_RDV_DTO jobRdvDto)
   @PutMapping("/updateJobRDv/{id}")
    @ResponseBody

    public Job_RDV updateJobRDV(@PathVariable("id") Long ID_Job_DRV, @RequestBody Job_RDV_DTO jobRdvDto )  {return  iJobRDV.updateJobRDV(ID_Job_DRV,jobRdvDto);}



    @DeleteMapping("deleteJobRDV")
    public  String deleteJobOffer( Long  ID_Job_DRV) {
        return iJobRDV.deleteJobOffer(ID_Job_DRV);

    }



    @PutMapping("AssignJobApplicationAndJuryToRDV/")
    public String AssignJobApplicationAndJuryToRDV(Long Id_Job_Application, Long ID_Job_DRV, Long idJury){
        return iJobRDV.AssignJobApplicationAndJuryToRDV(Id_Job_Application,ID_Job_DRV,idJury);


    }







    /*@GetMapping("/Determinate-Appointment-Date/{dispoCandidate}/{dispoJury}/{interviewDuration}")
    public ResponseEntity<LocalDateTime> findFirstAvailableDateTime(
            @ApiParam(value = "ID de la disponibilité du candidat", required = true) @PathVariable Long dispoCandidate,
            @ApiParam(value = "ID de la disponibilité du jury", required = true) @PathVariable Long dispoJury,
            @ApiParam(value = "Durée de l'entretien en minutes", required = true) @PathVariable int interviewDuration) {

        LocalDateTime firstAvailableDateTime = iJobRDV.findFirstAvailableDateTime(dispoCandidate,dispoJury,interviewDuration);

        if (firstAvailableDateTime == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(firstAvailableDateTime, HttpStatus.OK);
        }
    }*/

    /*@GetMapping("/CalculDistance/{id}")
    public double calculateDistance(@PathVariable("id") Long idRDV){
        return iJobRDV.calculateDistance(idRDV);

    }*/

    @GetMapping("/send-email-To-Fix-RDV-For-Interview/{id}")
    public void FixationRDV(@PathVariable("id") Long id) throws JsonProcessingException {
        iJobRDV.FixationRDV(id);

    }

    /*@PostMapping("/jobRDV/{id}/sendReminderSMS")
    public ResponseEntity<String> sendReminderSMS(@PathVariable(value = "id") Long id) {
        Job_RDV rdv = jobRdvRepository.findById(id).orElse(null);
        iJobRDV.sendReminderSMS(rdv);
        return ResponseEntity.ok().body("Reminder SMS sent successfully.");
    }*/

    @PostMapping("/send-reminders")
    public ResponseEntity<String> sendReminders() {
        try {
            jobRDVReminderTasklet.execute(null, null);
            return ResponseEntity.ok("Reminders sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending reminders: " + e.getMessage());
        }
    }








}
