package tn.esprit.springfever.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IEntretien;
import tn.esprit.springfever.entities.Entretien;
import tn.esprit.springfever.enums.Entretien_Res;
import tn.esprit.springfever.repositories.EntretienRepository;

import java.util.List;

@RestController
public class EntretienController {
    @Autowired
    IEntretien iEntretien;
    @Autowired
    EntretienRepository entretienRepository;




    @PostMapping("addEntretien/")
    public Entretien AddEntretien(@Validated @RequestBody Entretien entretien){
        return iEntretien.AddEntretien(entretien);


    }



    @GetMapping("getAllEntretiens/")
    public List<Entretien> GetAllEntretiens(){
        return iEntretien.GetAllEntretiens();

    }

    /*@PutMapping("updateEntretien/{id}")
    public Entretien UpdateEntretien(@PathVariable("id") Long ID_Job_Entretien ,@RequestBody Entretien entretien){
        return iEntretien.UpdateEntretien(ID_Job_Entretien,entretien);

    }*/


    @DeleteMapping("deleteEntretien/")
    public String DeleteEntretien(Long ID_Job_Entretien){
        return iEntretien.DeleteEntretien(ID_Job_Entretien);

    }
    /*@PutMapping("AssignRDVToEntretien/{ID_Job_Entretien}/{ID_Job_DRV}")
    public String AssignRDVToEntretien(@PathVariable("ID_Job_Entretien") Long ID_Job_Entretien ,@PathVariable("ID_Job_DRV") Long ID_Job_DRV ){
        return iEntretien.AssignRDVToEntretien(ID_Job_Entretien,ID_Job_DRV);

    }*/
    @PutMapping("AssignRDVToEntretien/")
    public String AssignRDVToEntretien(Long ID_Job_Entretien , Long ID_Job_DRV ){
        return iEntretien.AssignRDVToEntretien(ID_Job_Entretien,ID_Job_DRV);

    }
    @PostMapping("/send-emailToDistrubInterviewResult/")
    public ResponseEntity<String> sendEmailToDistrubInterviewResult(Long id) throws JsonProcessingException {
        Entretien entretien=entretienRepository.findById(id).orElse(null);
        if(entretien.getResultat()== Entretien_Res.ACCEPTED){
            String subject = "Result Of a Job Interview ";
            String text="Dear Candidate \n"+"Thank you for taking the time to interview with us for the Position role.\n"+
                    " We appreciate your interest in our camapny Esprit\n"+
                    "We are pleased to inform you that after careful consideration, you have been selected for the position.\n"+
                    " Congratulations! We believe that your skills and experience make you a great fit for our team, and we are excited to have you join us.\n"+"" +
                    "We will send you a separate email with details of the job offer, including salary, benefits, and start date.\n"+
                    " If you have any questions or concerns in the meantime, please do not hesitate to reach out to us.\n"+
                    "Once again, congratulations, and we look forward to working with you!\n"+
                    "Best Regards, \n"+
                    "ESPRIT \n";
             iEntretien.sendEmailToDistrubInterviewRes(id,subject,text);
            return ResponseEntity.ok("Email sent successfully!");



        }
        else{
            String subject = "Result Of a Job Interview ";
            String text ="Dear Candidate \n"+
                    "Thank you for taking the time to interview with us for the Position role.\n"+
                    "We appreciate your interest in our camapny Esprit\n"+
                    "After careful consideration, we regret to inform you that we have decided not to proceed with your application at this time\n"+
                    "We received a large number of qualified candidates for this position, and the competition was intense. " +
                    "Unfortunately, we have decided to pursue other candidates whose experience and skills more closely align with our needs.\n"+
                    "We appreciate the time and effort you invested in the interview process and wish you all the best in your future endeavors.\n"+
                    "Sincerely,\n"+
                    "ESPRIT";
            iEntretien.sendEmailToDistrubInterviewRes(id,subject,text);
            return ResponseEntity.ok("Email sent successfully!");


        }




    }
}
