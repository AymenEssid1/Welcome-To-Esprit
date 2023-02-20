package tn.esprit.springfever.repos;

import org.springframework.stereotype.Repository;
import tn.esprit.springfever.domain.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
}
