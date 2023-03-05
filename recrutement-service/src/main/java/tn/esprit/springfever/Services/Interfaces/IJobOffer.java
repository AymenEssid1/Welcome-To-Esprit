package tn.esprit.springfever.Services.Interfaces;

import com.rometools.rome.io.FeedException;
import tn.esprit.springfever.entities.Job_Offer;

import java.util.List;

public interface IJobOffer {
    public Job_Offer addJobOffer(Job_Offer job_offer);
    public List<Job_Offer> getAllJobOffers() ;
    public Job_Offer updateJobOffer(Long Id_Job_Offer , Job_Offer job_offer ) ;
    public  String deleteJobOffer(Long  Id_Job_Offer) ;
    public String AssignCategoryToJobOffer( Long Id_Job_Offer, Long Id_Job_Category);
    public String AssignJobApplicationToJobOffer(Long Id_Job_Offer,Long Id_Job_Application);

    public String AssignImageToJobOffer(Long Id_Job_Offer , Long Id_Job_Application );

    public String AssignUserToJobApplication(Long id , Long Id_Job_Application );

    public String generateRSSFeed() throws FeedException;
    public List<Object[]> countJobOffersByCategory() ;
    //public String getJobOffersSortedByApplicationCount();




}
