package tn.esprit.springfever.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.Services.Interfaces.IServiceFaqCategory;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.FaqCategory;
import tn.esprit.springfever.enums.Faq_Category_enum;

import java.util.List;


@RestController
@RequestMapping("/faq-categories")
public class FaqCategoryController {

    @Autowired
    private IServiceFaqCategory faqCategoryService;

    @PostMapping("addFaqCategory/add")
    public ResponseEntity<FaqCategory> addFaqCategory(@RequestBody FaqCategory faqCategory) {
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