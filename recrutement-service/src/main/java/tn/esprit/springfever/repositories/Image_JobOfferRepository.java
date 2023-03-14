package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Image_JobOffer;

@EnableJpaRepositories
public interface Image_JobOfferRepository extends JpaRepository<Image_JobOffer,Long> {
}
