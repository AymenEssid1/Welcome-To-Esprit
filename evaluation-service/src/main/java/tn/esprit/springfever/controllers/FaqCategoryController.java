package tn.esprit.springfever.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IServiceFaqCategory;
import tn.esprit.springfever.entities.FaqCategory;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import tn.esprit.springfever.Exceptions.ValidationExceptionHandler;
import javax.validation.Valid;

@RestController
@RequestMapping("/faq-categories")
public class FaqCategoryController {

    @Autowired
    private IServiceFaqCategory faqCategoryService;

    @PostMapping("addFaqCategory/add")
    public ResponseEntity<?> addFaqCategory(@Valid @RequestBody FaqCategory faqCategory, BindingResult result) {
        if (result.hasErrors()) {
            ValidationExceptionHandler.ValidationErrorResponse response = new ValidationExceptionHandler.ValidationErrorResponse();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError error : fieldErrors) {
                response.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        FaqCategory addedFaqCategory = faqCategoryService.addFaqCategory(faqCategory);
        return new ResponseEntity<>(addedFaqCategory, HttpStatus.CREATED);
    }


    @GetMapping("getAllFaqCategories/")
    public ResponseEntity<List<FaqCategory>> getAllFaqCategories() {
        List<FaqCategory> faqCategories = faqCategoryService.getAllFaqCategories();
        return new ResponseEntity<>(faqCategories, HttpStatus.OK);
    }

    @DeleteMapping("deleteFaqCategory/{id}")
    public ResponseEntity<String> deleteFaqCategory(@PathVariable Long id) {
        String message = faqCategoryService.deleteFaqCategory(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("updateFaqCategory/{id}")
    public ResponseEntity<FaqCategory> updateFaqCategory(@PathVariable Long id, @RequestBody FaqCategory faqCategory) {
        FaqCategory updatedFaqCategory = faqCategoryService.updateFaqCategory(id, faqCategory);
        return new ResponseEntity<>(updatedFaqCategory, HttpStatus.OK);
    }
}