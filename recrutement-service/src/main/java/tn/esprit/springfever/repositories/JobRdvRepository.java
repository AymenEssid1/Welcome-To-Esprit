package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Job_RDV;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@EnableJpaRepositories
public interface JobRdvRepository extends JpaRepository<Job_RDV,Long> {
    Job_RDV findJob_RDVByAppointmentDate(Date dateRDV);

    //Job_RDV findJob_RDVByCandidate_Id(Long candidateId);
    Job_RDV findJob_RDVByJury_Id(Long JuryId);
    //Job_RDV findJob_RDVByJobApplication_Id(Long Id_Job_Application);
    List<Job_RDV> findJob_RDVByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);




}
