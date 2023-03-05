package tn.esprit.springfever.repos;

import org.springframework.stereotype.Repository;
import tn.esprit.springfever.domain.DemandeAdmission;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface DemandeAdmissionRepository extends JpaRepository<DemandeAdmission, Long> {


}
