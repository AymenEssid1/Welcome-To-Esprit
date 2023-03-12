package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.FaqCategory;
import tn.esprit.springfever.enums.Faq_Category_enum;

import javax.validation.constraints.NotBlank;

@EnableJpaRepositories
public interface FaqCategoryRepository extends JpaRepository<FaqCategory,Long> {

public FaqCategory findByFaqCategory( Faq_Category_enum faqCategory);

}
