package tn.esprit.springfever.repos;

import tn.esprit.springfever.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
