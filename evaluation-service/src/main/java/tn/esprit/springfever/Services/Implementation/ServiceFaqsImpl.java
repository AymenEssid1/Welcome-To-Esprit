package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.repositories.UserRepository;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.FaqCategory;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.enums.Faq_Category_enum;
import tn.esprit.springfever.repositories.ClaimRepository;
import tn.esprit.springfever.repositories.FaqCategoryRepository;
import tn.esprit.springfever.repositories.FaqRepository;
import tn.esprit.springfever.repositories.UserRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class ServiceFaqsImpl  implements IServiceFaq {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FaqRepository faqRepository;
    @Autowired
    FaqCategoryRepository faqCategoryRepository;


    @Override
    public Faq addFaq(Faq faq) {
        log.info("Faq was successfully added !");
        return faqRepository.save(faq);
    }
    @Override
    public List<Faq> getAllFaqs() {
        log.info("list of FAQs  : !");
        return faqRepository.findAll();
    }
    @Override
    public String deleteFaq(Long idFaq) {
        Faq ExistingFaq = faqRepository.findById(idFaq).orElse(null) ;
        if(ExistingFaq!=null) {
            faqRepository.delete(ExistingFaq);
            log.info("Faq was successfully deleted !");
            return "Faq was successfully deleted !" ;
        }
        return "This Faq is not existing ! ";
    }
    @Override
    public Faq updateFaq(Long idFaq, Faq faq) {
        Faq existingFaq = faqRepository.findById(idFaq).orElse(null) ;
        if(existingFaq!=null) {
            existingFaq.setQuestion(faq.getQuestion());
            existingFaq.setResponse(faq.getResponse());
             faqRepository.save(existingFaq);
            log.info("Faq was successfully updated !");
        }
        log.info("This Faq is not existing !");

        return existingFaq;
    }

    @Override
    public List<Faq> getAllFaqsByCategory(Faq_Category_enum faq_category_enum) {
        log.info("faqs list by category :!" + faq_category_enum.toString());
        return faqRepository.getAllByFaqCategoriesFaqCategory(faq_category_enum);
    }

    @Override
    public String AssignCategoryToFaq( Long idFaq,Long idFaqCategory) {
        Faq existingFaq = faqRepository.findById(idFaq).orElse(null) ;
        FaqCategory ExistingFaqCategory = faqCategoryRepository.findById(idFaqCategory).orElse(null) ;

        if((existingFaq!=null) &&( ExistingFaqCategory!=null) ) {
            existingFaq.getFaqCategories().add(ExistingFaqCategory);
            faqRepository.save(existingFaq);
            log.info("FaqCategory  was successfully assigned to Faq !");
            return "FaqCategory  was successfully assigned to Faq !!" ;
        }
        return "Faq or Faq Category ot found ! ";
    }
}

