package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.springfever.entities.User;


import java.util.Optional;


@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<User,Long> {


 Optional<User> findByUsername(String username);

 Boolean existsByUsername(String username);

 Boolean existsByEmail(String email);



}
