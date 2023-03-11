package tn.esprit.springfever.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.springfever.entities.Badge;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.RoleType;

public interface BadgeRepo extends JpaRepository<Badge,Long> {

}
