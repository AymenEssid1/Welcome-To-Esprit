package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.Faq;
import  tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import  tn.esprit.springfever.entities.Faq;
import  tn.esprit.springfever.enums.Faq_Category_enum;

import java.util.List;
@RestController
@RequestMapping("/faqs")
public class FaqController {

    @Autowired
    private IServiceFaq faqService;

    @GetMapping("getAllFaqs/")
    public ResponseEntity<List<Faq>> getAllFaqs() {
        List<Faq> faqs = faqService.getAllFaqs();
        return new ResponseEntity<>(faqs, HttpStatus.OK);
    }

    @PostMapping("addFaq/")
    public ResponseEntity<Faq> addFaq(@RequestBody Faq faq) {
        Faq addedFaq = faqService.addFaq(faq);
        return new ResponseEntity<>(addedFaq, HttpStatus.CREATED);
    }

    @DeleteMapping("deleteFaq/{idFaq}")
    public ResponseEntity<String> deleteFaq(@PathVariable Long idFaq) {
        String message = faqService.deleteFaq(idFaq);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("updateFaq/{idFaq}")
    public ResponseEntity<Faq> updateFaq(@PathVariable Long idFaq, @RequestBody Faq faq) {
        Faq updatedFaq = faqService.updateFaq(idFaq, faq);
        return new ResponseEntity<>(updatedFaq, HttpStatus.OK);
    }

    @GetMapping("/getAllFaqsByCategory/{category}")
    public ResponseEntity<List<Faq>> getAllFaqsByCategory(@PathVariable Faq_Category_enum category) {
        List<Faq> faqs = faqService.getAllFaqsByCategory(category);
        return new ResponseEntity<>(faqs, HttpStatus.OK);
    }
    @PutMapping("AssignCategoryToFaq/{idFaq}/{idFaqCategory}")
    public ResponseEntity<String> AssignCategoryToFaq(@PathVariable Long idFaq,@PathVariable Long idFaqCategory ) {
            String  updatedFaqStatus = faqService.AssignCategoryToFaq(idFaq, idFaqCategory);
        return new ResponseEntity<>(updatedFaqStatus, HttpStatus.OK);
    }

}