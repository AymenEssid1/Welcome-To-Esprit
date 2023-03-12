package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.FaqCategory;
import tn.esprit.springfever.enums.Faq_Category_enum;
import tn.esprit.springfever.repositories.FaqCategoryRepository;
import tn.esprit.springfever.repositories.UserRepository;
import tn.esprit.springfever.Services.Interfaces.IServiceFaqCategory;

import java.util.List;

@Service
@Slf4j
public class ServiceFaqCategoriesImpl implements IServiceFaqCategory {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FaqCategoryRepository faqCategoryRepository;

    @Override
    public FaqCategory addFaqCategory(FaqCategory faqCategory) {
        log.info("Faq Category was successfully added !");
        return faqCategoryRepository.save(faqCategory);
    }

    @Override
    public List<FaqCategory> getAllFaqCategories() {
        log.info("list of FAQs Categories  : !");
        return faqCategoryRepository.findAll();
    }

    @Override
    public String deleteFaqCategory(Long idFaqCategory) {
        FaqCategory ExistingFaqCategory = faqCategoryRepository.findById(idFaqCategory).orElse(null) ;
        if(ExistingFaqCategory!=null) {
            faqCategoryRepository.delete(ExistingFaqCategory);
            log.info("ExistingFaqCategory was successfully deleted !");
            return "ExistingFaqCategory was successfully deleted !" ;
        }
        return "This ExistingFaqCategory is not existing ! ";
    }

    @Override
    public FaqCategory updateFaqCategory(Long idFaqCategory, FaqCategory faqCategory) {
        FaqCategory existingFaqCategory = faqCategoryRepository.findById(idFaqCategory).orElse(null) ;
        if(existingFaqCategory!=null) {
            existingFaqCategory.setFaqCategory(faqCategory.getFaqCategory());
            faqCategoryRepository.save(existingFaqCategory);
            log.info("Faq Category  was successfully updated !");
        }
        log.info("This Faq Category is not existing !");
        return existingFaqCategory;
    }


    @Override
    public FaqCategory veriforAdd(Faq_Category_enum faq_category_enum) {

        FaqCategory faqCategory = faqCategoryRepository.findByFaqCategory(faq_category_enum);

        return null;
    }


}

