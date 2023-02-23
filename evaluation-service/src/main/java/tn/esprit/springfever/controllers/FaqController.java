package tn.esprit.springfever.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.Faq;
import  tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import  tn.esprit.springfever.entities.Faq;
import  tn.esprit.springfever.enums.Faq_Category_enum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/faqs")
@Slf4j
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

    @PostMapping(value="/import" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<List<Faq>> importFAQs(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            List<Faq> faqs =faqService.importFAQsFromExcel(file);
            log.info("FAQs imported successfully");
            return ResponseEntity.ok(faqs);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    @GetMapping("/SearchFaqs")
    public ResponseEntity<List<Faq>> searchFaqs(@RequestParam("q") String query) {
        List<Faq> faqs = faqService.search(query);
        return ResponseEntity.ok(faqs);
    }

    @GetMapping("/topSearchedQueries")
    public ResponseEntity<List<String>> topSearchedQueries() {
        List<String> faqs = faqService.topSearchedQueries();
        return ResponseEntity.ok(faqs);
    }
    @GetMapping("/getDfaultFaqs")
    public ResponseEntity<List<Faq>> getDfaultFaqs() {
        List<Faq> faqs = faqService.getDfaultFaqs();
        return ResponseEntity.ok(faqs);
    }







}