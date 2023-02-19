package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Job_Application;

import java.util.List;

public interface IJobApplication {
    public Job_Application AddJobApplication (Job_Application job_application);

    public List<Job_Application> GetAllJobApplications();

    public String DeleteJobApplication (Long Id_Job_Application);

   // public Job_Application UpdateJobApplication(Long Id_Job_Application , Job_Application job_application);
   public Job_Application save(byte[] cv, byte[] lettre, String imageName) throws Exception ;
}
