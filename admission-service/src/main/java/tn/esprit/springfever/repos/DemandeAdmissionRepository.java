package tn.esprit.springfever.repos;

import org.springframework.stereotype.Repository;
import tn.esprit.springfever.domain.DemandeAdmission;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springfever.domain.Salle;
import tn.esprit.springfever.domain.User;
import tn.esprit.springfever.model.Diplome;

import java.time.LocalDate;

@Repository
public interface DemandeAdmissionRepository extends JpaRepository<DemandeAdmission, Long> {

    public DemandeAdmission findByRdvDemandeSalle(Salle rdvDemande_salle) ;

    public DemandeAdmission findDemandeAdmissionByEvaluateurAndDateAdmission(User evaluateur, LocalDate dateAdmission) ;

    Long countByDiplome(Diplome PREPA);

}
