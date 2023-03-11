package tn.esprit.springfever.Services.Implementation;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IJobOffer;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.repositories.*;

import java.io.IOException;
import java.util.List;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.io.SyndFeedOutput;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.repositories.JobOfferRepository;
import com.rometools.rome.feed.synd.SyndEntryImpl;

import java.io.StringWriter;


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


    public String AssignImageAndJobCategoryToJobOffer(Long Id_Job_Offer , Long id , Long Id_Job_Category ){
        Job_Offer job_offer =jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        Image_JobOffer image_jobOffer=image_jobOfferRepository.findById(id).orElse(null);
        Job_Category JobCategoryExisted = jobCategoryRepository.findById(Id_Job_Category).orElse(null);
        if(job_offer!=null && image_jobOffer!=null && JobCategoryExisted!=null){
            job_offer.setImage(image_jobOffer);
            job_offer.setJobCategory(JobCategoryExisted);
            jobOfferRepository.save(job_offer);
            return "Image And JobCategory Are  successffully affected To Job Offer ! ";
        }
        return "Job Offer Or Image Or JobCategory Does not Exist ";
    }


    /*public String AssignJobOfferAndCandidateToJobApplication(Long Id_Job_Offer,Long Id_Job_Application, Long idUser){
        Job_Offer job_offer=jobOfferRepository.findById(Id_Job_Offer).orElse(null);
        Job_Application job_application=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        User Candidate=userRepository.findById(idUser).orElse(null);
        if(job_offer!=null && job_application!=null &&Candidate !=null){
            job_application.setJobOffer(job_offer);
            job_application.setUser(Candidate);
            jobApplicationRepository.save(job_application);
            return "Candidate and Job Offer are successffully assigned ";
        }
        return "Job Offer Or Job Application Or Candidate are not found ! ";
    }*/

    /*public String AssignUserToJobApplication(Long id , Long Id_Job_Application ){
        User user=userRepository.findById(id).orElse(null);
        Job_Application job_application=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        if(job_application!=null && user!=null){
            job_application.setUser(user);
            jobApplicationRepository.save(job_application);
            return "User is Affected To Job Application  Sucessffully !";

        }
        return "User Or Job Application are not Fouund !";


    }*/


    public String generateRSSFeed() throws FeedException {
        List<Job_Offer> jobOffers = jobOfferRepository.findAll();

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType("rss_2.0");
        feed.setTitle("Job Offers");
        feed.setDescription("List of available job offers");
        feed.setLink("https://example.com/job-offers");

        for (Job_Offer jobOffer : jobOffers) {
            SyndEntryImpl entry = new SyndEntryImpl();
            entry.setTitle(jobOffer.getJobCategory().getName_Category());
            entry.setLink("https://example.com/job-offers/" + jobOffer.getId_Job_Offer());
            SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(jobOffer.getSubject());
            entry.setDescription(description);
            //entry.setPublishedDate(jobOffer.getCreationDate());
            feed.getEntries().add(entry);
        }

        StringWriter writer = new StringWriter();
        SyndFeedOutput output = new SyndFeedOutput();
        try {
            output.output(feed, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }

    public List<Object[]> countJobOffersByCategory() {
        return jobOfferRepository.countJobOffersByCategory();
    }
    /*public String getJobOffersSortedByApplicationCount() {
        System.out.println("Ok1");
        String res="";
        try {
            List<Job_Offer> list = jobOfferRepository.findAllSortedByApplicationCount();
            System.out.println("Ok 2");
            System.out.println(list.get(0).toCustomString());

            System.out.println(list.get(0));
            for (int i = 0; i < list.size(); i++) {
                res=res+list.get(i)+"\n";
                //System.out.println(list.get(i));

            }
            return res;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }*/



}
