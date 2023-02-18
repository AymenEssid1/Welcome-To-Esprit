package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.entities.Faq;
 import tn.esprit.springfever.entities.FaqCategory;
 import tn.esprit.springfever.enums.Faq_Category_enum;

 import java.util.List;

public interface IServiceFaq {

 public Faq addFaq(Faq faq) ;
 public  List<Faq> getAllFaqs() ;
 public  String deleteFaq(Long idFaq) ;
 public Faq updateFaq(Long idFaq , Faq faq ) ;
 public List<Faq> getAllFaqsByCategory(Faq_Category_enum faq_category_enum) ;
 public String AssignCategoryToFaq( Long idFaq, Long idFaqCategory);


}
