package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IJobCategory;
import tn.esprit.springfever.entities.Job_Category;
import tn.esprit.springfever.repositories.JobCategoryRepository;

import java.util.List;

@Service
@Slf4j
public class JobCategoryService implements IJobCategory {
    @Autowired
    JobCategoryRepository jobCategoryRepository ;

    public Job_Category addJobCategory(Job_Category JobCategory){
        return jobCategoryRepository.save(JobCategory);


    }

    public Job_Category updateJobCategory(Long Id_Job_Category , Job_Category job_category ){
        Job_Category Job_CategoryExisted = jobCategoryRepository.findById(Id_Job_Category).orElse(null);
        if(Job_CategoryExisted!=null){
            Job_CategoryExisted.setName_Category(job_category.getName_Category());
            jobCategoryRepository.save(Job_CategoryExisted);
            log.info("Job_Category is Updated Successfully!");
            return Job_CategoryExisted;
        }
        log.info("JobCategory does not Exit ! ");
        return Job_CategoryExisted;
    }

    public List<Job_Category> getAllJobCategories(){

        return jobCategoryRepository.findAll();
    }
    public  String deleteJobCategory(Long Id_Job_Category ) {
        Job_Category Job_CategoryExisted = jobCategoryRepository.findById(Id_Job_Category).orElse(null);
        if(Job_CategoryExisted!=null){
            jobCategoryRepository.delete(Job_CategoryExisted);
            log.info("Job Category is deleted ! ");
            return  "Job Category is deleted ! ";
        }
        return "Job Category does not exist ! ";


    }
}
