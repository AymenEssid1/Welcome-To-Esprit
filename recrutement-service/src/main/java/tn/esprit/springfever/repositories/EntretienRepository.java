package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Entretien;

@EnableJpaRepositories
@Repository
public interface EntretienRepository extends JpaRepository<Entretien,Long> {
}
