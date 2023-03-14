package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.entities.FaqCategory;
import tn.esprit.springfever.enums.Faq_Category_enum;

import java.util.List;

public interface IServiceFaqCategory {

 public FaqCategory addFaqCategory(FaqCategory faqCategory) ;
 public  List<FaqCategory> getAllFaqCategories() ;
 public  String deleteFaqCategory(Long idFaqCategory) ;
 public FaqCategory updateFaqCategory(Long FaqCategory , FaqCategory faqCategory ) ;
 FaqCategory veriforAdd(Faq_Category_enum faq_category_enum);
}
