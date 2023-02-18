package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.FaqCategory;

@EnableJpaRepositories
public interface FaqCategoryRepository extends JpaRepository<FaqCategory,Long> {


}
