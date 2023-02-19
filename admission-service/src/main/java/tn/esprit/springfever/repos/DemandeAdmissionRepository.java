package tn.esprit.springfever.repos;

import tn.esprit.springfever.domain.DemandeAdmission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DemandeAdmissionRepository extends JpaRepository<DemandeAdmission, Long> {
}
