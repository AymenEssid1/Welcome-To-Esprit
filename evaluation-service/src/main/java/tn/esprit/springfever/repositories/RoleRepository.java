package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.enums.ERole;
import tn.esprit.springfever.entities.RoleEvaluation;

import java.util.Optional;


@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<RoleEvaluation,Long> {


 Optional<RoleEvaluation> findByName(ERole name);


}
