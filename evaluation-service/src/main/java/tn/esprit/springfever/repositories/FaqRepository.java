package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.enums.Faq_Category_enum;

import javax.validation.constraints.NotBlank;
import java.util.List;

@EnableJpaRepositories
public interface FaqRepository extends JpaRepository<Faq,Long> {

    public List<Faq> getAllByFaqCategoriesFaqCategory(Faq_Category_enum faqCategories_faqCategory);
}
