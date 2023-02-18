package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.entities.FaqCategory;

import java.util.List;

public interface IServiceFaqCategory {

 public FaqCategory addFaqCategory(FaqCategory faqCategory) ;
 public  List<FaqCategory> getAllFaqCategories() ;
 public  String deleteFaqCategory(Long idFaqCategory) ;
 public FaqCategory updateFaqCategory(Long FaqCategory , FaqCategory faqCategory ) ;

}
