package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.Question;
import tn.esprit.springfever.enums.Faq_Category_enum;

import java.util.List;

@EnableJpaRepositories
public interface FaqRepository extends JpaRepository<Faq,Long> {

    public List<Faq> getAllByFaqCategoriesFaqCategory(Faq_Category_enum faqCategories_faqCategory);
    @Query("SELECT f FROM Faq f WHERE f.question LIKE %?1% OR f.response LIKE %?1%")
    List<Faq> search(String query);


    @Query("SELECT q FROM Faq q WHERE q.question LIKE %:keyword% OR q.response LIKE %:keyword%")
    List<Faq> findByKeywords(@Param("keyword") String keywords);

}
