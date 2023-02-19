package tn.esprit.springfever.repos;

import tn.esprit.springfever.domain.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
}
