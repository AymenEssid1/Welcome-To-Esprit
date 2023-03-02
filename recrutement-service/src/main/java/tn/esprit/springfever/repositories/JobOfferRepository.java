package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.entities.Job_RDV;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface JobOfferRepository extends JpaRepository<Job_Offer,Long> {

    @Query("SELECT j.jobCategory.Name_Category, COUNT(j.Id_Job_Offer) FROM Job_Offer j GROUP BY j.jobCategory.Id_Job_Category")
    List<Object[]> countJobOffersByCategory();

}
