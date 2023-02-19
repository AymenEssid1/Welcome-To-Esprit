package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IJobRDV;
import tn.esprit.springfever.entities.Job_RDV;

import java.util.List;

@RestController
public class JobRdvController {
    @Autowired
    IJobRDV iJobRDV;






    @PostMapping("addJobRDV/")
    public Job_RDV addJobRDV(@RequestBody Job_RDV job_rdv){
        return iJobRDV.addJobRDV(job_rdv);

    }



    @GetMapping("getAllJobRDVS/")
    public List<Job_RDV> getAllJobRDVs() {
        return iJobRDV.getAllJobRDVs();

    }

    @PutMapping("updateJobRDV/{id}")
    public Job_RDV updateJobRDV (@PathVariable("id") Long ID_Job_DRV , @RequestBody Job_RDV job_rdv ) {
        return iJobRDV.updateJobRDV(ID_Job_DRV,job_rdv);

    }


    @DeleteMapping("deleteJobRDV/{id}")
    public  String deleteJobOffer(@PathVariable("id") Long  ID_Job_DRV) {
        return iJobRDV.deleteJobOffer(ID_Job_DRV);

    }

    @PutMapping("AssignEntretienToRDV/{ID_Job_Entretien}/{ID_Job_DRV}")
    public String AssignEntretienToRDV( @PathVariable("ID_Job_Entretien") Long ID_Job_Entretien, @PathVariable("ID_Job_DRV") Long ID_Job_DRV){
        return iJobRDV.AssignEntretienToRDV(ID_Job_Entretien,ID_Job_DRV);

    }

    @PutMapping("AssignJobApplicationToRDV/{Id_Job_Application}/{ID_Job_DRV}")
    public String AssignJobApplicationToRDV(@PathVariable("Id_Job_Application") Long Id_Job_Application , @PathVariable("ID_Job_DRV") Long ID_Job_DRV){
        return iJobRDV.AssignJobApplicationToRDV(Id_Job_Application,ID_Job_DRV);


    }
}
