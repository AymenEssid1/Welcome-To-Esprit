package tn.esprit.springfever.repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.domain.Salle;
import tn.esprit.springfever.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByetatuser(String etatuser);

    @Query("SELECT u FROM User u JOIN u.rDVuserRDVs r WHERE u.etatuser = 'non disponible' AND r.date = :date")

    List<User> findByetatuserAndRDVuserRDVsDate();
}
