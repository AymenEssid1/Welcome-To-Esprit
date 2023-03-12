package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IJobCategory;
import tn.esprit.springfever.entities.Job_Category;

import javax.validation.Valid;
import java.util.List;

@RestController
public class JobCategoryController {
    @Autowired
    IJobCategory iJobCategory;

    /*@PostMapping("/addCategory")
    @ResponseBody
    public Job_Category addJobCategory(@Validated @RequestBody Job_Category JobCategory){
        return iJobCategory.addJobCategory(JobCategory);
    }*/
    @PostMapping("/addCategory")
    @ResponseBody
    public ResponseEntity<?> addJobCategory(@Valid @RequestBody Job_Category jobCategory, BindingResult result) {
        if(result.hasErrors()) {
            ValidationExceptionHandler.ValidationErrorResponse response = new ValidationExceptionHandler.ValidationErrorResponse();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for(FieldError error : fieldErrors) {
                response.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Job_Category addedCategory = iJobCategory.addJobCategory(jobCategory);
        return new ResponseEntity<>(addedCategory, HttpStatus.OK);
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
