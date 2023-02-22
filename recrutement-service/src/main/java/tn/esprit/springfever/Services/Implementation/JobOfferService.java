package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IJobOffer;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.*;

import java.util.List;

@Service
@Slf4j
public class JobOfferService implements IJobOffer {

    @Autowired
    JobOfferRepository jobOfferRepository;
    @Autowired
    JobCategoryRepository jobCategoryRepository;

    @Autowired
    Image_JobOfferRepository image_jobOfferRepository;

    @Autowired
    JobApplicationRepository jobApplicationRepository;
    @Autowired
    UserRepository userRepository;

    public Job_Offer addJobOffer(Job_Offer job_offer){
        return jobOfferRepository.save(job_offer);
    }
    public List<Job_Offer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public Job_Offer updateJobOffer(Long Id_Job_Offer , Job_Offer job_offer ){
        Job_Offer jobOfferExisted = jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        if(jobOfferExisted!=null){
            jobOfferExisted.setSubject(job_offer.getSubject());
            jobOfferExisted.setJobCategory(job_offer.getJobCategory());
            jobOfferExisted.setImage(job_offer.getImage());
            jobOfferRepository.save(jobOfferExisted);
            log.info("Job Offer is updated !");
            return jobOfferExisted;

        }
        log.info("Job Offer Does not Exist ! ");
        return jobOfferExisted;
    }
    public  String deleteJobOffer(Long  Id_Job_Offer){
        Job_Offer jobOfferExisted=jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        if(jobOfferExisted!=null){
            jobOfferRepository.delete(jobOfferExisted);
            log.info("Job Offer deleted ! ");
            return "Job Offer deleted ! ";
        }
        return "job Offer Does not Exist !";
    }
    public String AssignCategoryToJobOffer( Long Id_Job_Offer, Long Id_Job_Category){
        Job_Offer jobOfferExisted=jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        Job_Category JobCategoryExisted = jobCategoryRepository.findById(Id_Job_Category).orElse(null);
        if(jobOfferExisted !=null && JobCategoryExisted!=null){
            JobCategoryExisted.getJobOffers().add(jobOfferExisted);
            jobOfferExisted.setJobCategory(JobCategoryExisted);
            jobOfferRepository.save(jobOfferExisted);
            return "Job Offer is sucessffully affected To Job Job Category !";
        }
        return "Job Offer or Job Catgory Does not exist !";

    }

    public String AssignImageToJobOffer(Long Id_Job_Offer , Long id ){
        Job_Offer job_offer =jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        Image_JobOffer image_jobOffer=image_jobOfferRepository.findById(id).orElse(null);
        if(job_offer!=null && image_jobOffer!=null){
            job_offer.setImage(image_jobOffer);
            jobOfferRepository.save(job_offer);
            return "Image Is successffully affected To Job Offer ! ";
        }
        return "Job Offer Or Image Does not Exist ";
    }


    public String AssignJobApplicationToJobOffer(Long Id_Job_Offer,Long Id_Job_Application){
        Job_Offer job_offer=jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        Job_Application job_application=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        if(job_offer!=null && job_application!=null){
            job_application.setJobOffer(job_offer);
            jobApplicationRepository.save(job_application);
            return "Job Application is succesffuly affected to job Offer !";
        }
        return "Job Offer Or Job Application are not found ! ";
    }

    public String AssignUserToJobApplication(Long id , Long Id_Job_Application ){
        User user=userRepository.findById(id).orElse(null);
        Job_Application job_application=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        if(job_application!=null && user!=null){
            job_application.setUser(user);
            jobApplicationRepository.save(job_application);
            return "User is Affected To Job Application  Sucessffully !";

        }
        return "User Or Job Application are not Fouund !";


    }
}
