package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Job_Application;

@EnableJpaRepositories
@Repository
public interface JobApplicationRepository extends JpaRepository<Job_Application,Long> {

}
