package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.enums.ERole;

import java.util.Optional;


@EnableJpaRepositories
public interface RoleRepository extends JpaRepository<Role,Long> {


 Optional<Role> findByName(ERole name);


}
