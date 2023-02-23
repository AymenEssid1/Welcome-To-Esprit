package tn.esprit.springfever.Services.Interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import tn.esprit.springfever.entities.Image_JobOffer;
import tn.esprit.springfever.entities.Job_Application;

import java.util.List;

public interface IJobApplication {
    public Job_Application AddJobApplication (Job_Application job_application);

    public List<Job_Application> GetAllJobApplications();

    public String DeleteJobApplication (Long Id_Job_Application);

   // public Job_Application UpdateJobApplication(Long Id_Job_Application , Job_Application job_application);
   public Job_Application save(byte[] cv, byte[] lettre, String imageName) throws Exception ;
    public Job_Application savef(byte[] cv, byte[] lettre, String location_Cv,String location_LettreMotivation ) throws Exception ;
    public FileSystemResource findCV(Long Id_Job_Application);
    public FileSystemResource findLettreMotivation(Long Id_Job_Application);

    //public Resource[] find(Long Id_Job_Application);
    public String FilterCv(Long Id_Job_Application);
    public String extractTextFromPdf(Long id);

}
