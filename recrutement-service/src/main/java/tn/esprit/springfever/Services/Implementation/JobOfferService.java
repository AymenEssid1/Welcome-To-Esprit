package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IJobOffer;
import tn.esprit.springfever.entities.Image_JobOffer;
import tn.esprit.springfever.entities.Job_Category;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.repositories.Image_JobOfferRepository;
import tn.esprit.springfever.repositories.JobCategoryRepository;
import tn.esprit.springfever.repositories.JobOfferRepository;

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
}
