package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IJobCategory;
import tn.esprit.springfever.entities.Job_Category;

import java.util.List;

@RestController
public class JobCategoryController {
    @Autowired
    IJobCategory iJobCategory;

    @PostMapping("/addCategory")
    @ResponseBody
    public Job_Category addJobCategory(@RequestBody Job_Category JobCategory){
        return iJobCategory.addJobCategory(JobCategory);
    }
    @PutMapping("updateJobCategory/{id}")
    public Job_Category updateJobCategory(@PathVariable("id") Long Id_Job_Category , @RequestBody Job_Category job_category ){
        return  iJobCategory.updateJobCategory(Id_Job_Category,job_category);

    }
    @GetMapping("getAllJobCategories/")
    public List<Job_Category> getAllJobCategories(){
        return iJobCategory.getAllJobCategories();

    }
    @DeleteMapping("deleteJobCategory/{id}")
    public  String deleteJobCategory(@PathVariable("id") Long Id_Job_Category ){
        return iJobCategory.deleteJobCategory(Id_Job_Category);

    }
}
