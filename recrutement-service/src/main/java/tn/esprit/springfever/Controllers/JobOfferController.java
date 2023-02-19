package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IJobOffer;
import tn.esprit.springfever.entities.Job_Offer;

import java.util.List;

@RestController
public class JobOfferController  {
    @Autowired
    IJobOffer iJobOffer;
    @PostMapping("addJobOffer/")
    public Job_Offer addJobOffer(@RequestBody Job_Offer job_offer){
        return iJobOffer.addJobOffer(job_offer);

    }
    @GetMapping("getAllJobOffers/")
    public List<Job_Offer> getAllJobOffers(){
        return iJobOffer.getAllJobOffers();
    }

    @PutMapping("updateJobOffer/{id}")
    public Job_Offer updateJobOffer(@PathVariable("id") Long Id_Job_Offer ,@RequestBody Job_Offer job_offer ){
        return iJobOffer.updateJobOffer(Id_Job_Offer,job_offer);

    }



    @DeleteMapping("deleteJobOffer/{id}")
    public  String deleteJobOffer(@PathVariable("id") Long  Id_Job_Offer){
        return iJobOffer.deleteJobOffer(Id_Job_Offer);
    }
    @PutMapping("AssignJobOfferToCategory/{Id_Job_Offer}/{Id_Job_Category}")
    public String AssignCategoryToJobOffer( Long Id_Job_Offer, Long Id_Job_Category){
        return iJobOffer.AssignCategoryToJobOffer(Id_Job_Offer,Id_Job_Category);

    }


}
