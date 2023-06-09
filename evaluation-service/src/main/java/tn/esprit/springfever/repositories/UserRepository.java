package tn.esprit.springfever.repositories;

 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
 import tn.esprit.springfever.entities.UserEvaluation;

 import java.util.Optional;


@EnableJpaRepositories
@Repository
public interface UserRepository extends JpaRepository<UserEvaluation,Long> {


 Optional<UserEvaluation> findByUsername(String username);

 Boolean existsByUsername(String username);

 Boolean existsByEmail(String email);



}
