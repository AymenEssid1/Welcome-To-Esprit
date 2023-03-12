package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Job_Category;

@EnableJpaRepositories
public interface JobCategoryRepository extends JpaRepository<Job_Category,Long> {
}
