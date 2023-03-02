package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Job_Application;

import java.util.List;

@EnableJpaRepositories
@Repository
public interface JobApplicationRepository extends JpaRepository<Job_Application,Long> {
    @Query(value = "SELECT jo.Id_Job_Offer, jo.Title, jo.Subject, " +
            "(SELECT COUNT(*) FROM Job_Application ja WHERE ja.Id_Job_Offer = jo.Id_Job_Offer) AS applicationCount " +
            "FROM Job_Offer jo " +
            "GROUP BY jo.Id_Job_Offer",
            nativeQuery = true)
    List<Object[]> getJobOfferApplicationCount();

}
