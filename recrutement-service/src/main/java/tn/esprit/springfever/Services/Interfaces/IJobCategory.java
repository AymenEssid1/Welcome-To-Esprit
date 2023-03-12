package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Job_Category;

import java.util.List;

public interface IJobCategory {
    public Job_Category addJobCategory(Job_Category JobCategory) ;
    public Job_Category updateJobCategory(Long Id_Job_Category , Job_Category job_category ) ;
    public List<Job_Category> getAllJobCategories();
    public  String deleteJobCategory(Long Id_Job_Category ) ;
}
