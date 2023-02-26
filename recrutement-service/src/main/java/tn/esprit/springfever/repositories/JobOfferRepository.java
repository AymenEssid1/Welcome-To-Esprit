package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.entities.Job_RDV;

@EnableJpaRepositories
@Repository
public interface JobOfferRepository extends JpaRepository<Job_Offer,Long> {

}
